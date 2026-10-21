package org.unibl.etf.emobility_hub.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.unibl.etf.emobility_hub.controllers.AuthController;
import org.unibl.etf.emobility_hub.controllers.PromotionController;
import org.unibl.etf.emobility_hub.controllers.UserController;
import org.unibl.etf.emobility_hub.exception.BadRequestException;
import org.unibl.etf.emobility_hub.exception.EntityNotFoundException;
import org.unibl.etf.emobility_hub.models.domain.entity.UserEntity;
import org.unibl.etf.emobility_hub.models.domain.value.RoleEnum;
import org.unibl.etf.emobility_hub.models.dto.response.AuthResponse;
import org.unibl.etf.emobility_hub.models.dto.response.PromotionResponse;
import org.unibl.etf.emobility_hub.security.auth.IAuthenticationService;
import org.unibl.etf.emobility_hub.security.config.CorsConfig;
import org.unibl.etf.emobility_hub.security.config.SecurityConfig;
import org.unibl.etf.emobility_hub.security.config.UserDetailsServiceImpl;
import org.unibl.etf.emobility_hub.security.jwt.JwtService;
import org.unibl.etf.emobility_hub.security.jwt.SessionCookies;
import jakarta.servlet.http.Cookie;
import java.time.Instant;
import org.unibl.etf.emobility_hub.services.IPromotionService;
import org.unibl.etf.emobility_hub.services.IUserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Real SecurityConfig + JWT filter + GlobalExceptionHandler against mocked services.
@WebMvcTest(controllers = {PromotionController.class, AuthController.class, UserController.class})
@Import({SecurityConfig.class, CorsConfig.class, JwtService.class, SessionCookies.class})
class SecurityAndErrorHandlingTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    JwtService jwtService;

    @MockitoBean
    UserDetailsServiceImpl userDetailsService;
    @MockitoBean
    IPromotionService promotionService;
    @MockitoBean
    IAuthenticationService authenticationService;
    @MockitoBean
    IUserService userService;

    static UserEntity user(String username, RoleEnum role) {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword("x");
        user.setRole(role);
        return user;
    }

    @BeforeEach
    void users() {
        when(userDetailsService.loadUserByUsername("manager")).thenReturn(user("manager", RoleEnum.ROLE_MANAGER));
        when(userDetailsService.loadUserByUsername("operator")).thenReturn(user("operator", RoleEnum.ROLE_OPERATOR));
        when(userDetailsService.loadUserByUsername("ghost")).thenThrow(new EntityNotFoundException("gone"));
        when(promotionService.findAll(any())).thenReturn(Page.empty());
    }

    String bearer(String username, RoleEnum role) {
        return "Bearer " + jwtService.generateToken(user(username, role));
    }

    Cookie session(String username, RoleEnum role) {
        return new Cookie("EMH_SESSION", jwtService.generateToken(user(username, role)));
    }

    // ── Authorization rules ────────────────────────────────────────────────

    @Test
    void promotionListNeedsAToken() throws Exception {
        mvc.perform(get("/promotions")).andExpect(status().isUnauthorized());
    }

    @Test
    void promotionListIsForbiddenToAnOperator() throws Exception {
        mvc.perform(get("/promotions").header("Authorization", bearer("operator", RoleEnum.ROLE_OPERATOR)))
                .andExpect(status().isForbidden());
    }

    @Test
    void promotionListIsOpenToAManager() throws Exception {
        mvc.perform(get("/promotions").header("Authorization", bearer("manager", RoleEnum.ROLE_MANAGER)))
                .andExpect(status().isOk());
    }

    @Test
    void aSinglePromotionIsPublic() throws Exception {
        PromotionResponse promotion = new PromotionResponse();
        promotion.setId(1L);
        promotion.setTitle("Weekend Car Special");
        when(promotionService.getById(1L)).thenReturn(promotion);

        mvc.perform(get("/promotions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Weekend Car Special"));
    }

    // ── Broken tokens must never cause a 500 ───────────────────────────────

    @Test
    void aTamperedTokenOnAPublicEndpointIsIgnored() throws Exception {
        when(promotionService.getById(1L)).thenReturn(new PromotionResponse());

        mvc.perform(get("/promotions/1").header("Authorization", "Bearer not.a.jwt"))
                .andExpect(status().isOk());
    }

    @Test
    void aTamperedTokenOnAProtectedEndpointIsUnauthorized() throws Exception {
        String token = bearer("manager", RoleEnum.ROLE_MANAGER);
        String tampered = token.substring(0, token.length() - 4) + "AAAA";

        mvc.perform(get("/promotions").header("Authorization", tampered))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void aTokenForADeletedUserIsUnauthorized() throws Exception {
        mvc.perform(get("/promotions").header("Authorization", bearer("ghost", RoleEnum.ROLE_MANAGER)))
                .andExpect(status().isUnauthorized());
    }

    // ── Login ──────────────────────────────────────────────────────────────

    @Test
    void loginReturnsTheToken() throws Exception {
        when(authenticationService.login(any())).thenReturn(AuthResponse.builder()
                .token("jwt").username("admin").role("ROLE_ADMIN").expiresAt(Instant.now().plusSeconds(3600)).build());

        mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"admin\",\"password\":\"admin123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt"))
                .andExpect(jsonPath("$.role").value("ROLE_ADMIN"))
                .andExpect(cookie().value("EMH_SESSION", "jwt"))
                .andExpect(cookie().httpOnly("EMH_SESSION", true))
                .andExpect(cookie().sameSite("EMH_SESSION", "Strict"));
    }

    @Test
    void wrongCredentialsAreUnauthorizedNotAServerError() throws Exception {
        when(authenticationService.login(any())).thenThrow(new BadCredentialsException("Bad credentials"));

        mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"admin\",\"password\":\"wrong\"}"))
                .andExpect(status().isUnauthorized());
    }

    // ── GlobalExceptionHandler ─────────────────────────────────────────────

    @Test
    void validationErrorsAreReturnedPerField() throws Exception {
        mvc.perform(post("/promotions").header("Authorization", bearer("manager", RoleEnum.ROLE_MANAGER))
                        .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.startDate").exists());
    }

    @Test
    void missingEntityIsNotFound() throws Exception {
        doThrow(new EntityNotFoundException("Promotion with ID 7 not found")).when(promotionService).delete(7L);

        mvc.perform(delete("/promotions/7").header("Authorization", bearer("manager", RoleEnum.ROLE_MANAGER)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Promotion with ID 7 not found"));
    }

    @Test
    void badRequestExceptionIsABadRequest() throws Exception {
        when(userService.create(any())).thenThrow(new BadRequestException("Invalid role: BOSS"));

        mvc.perform(post("/users/register").header("Authorization", bearer("manager", RoleEnum.ROLE_MANAGER))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"x\",\"firstName\":\"X\",\"lastName\":\"Y\",\"password\":\"p\",\"role\":\"boss\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid role: BOSS"));
    }

    // ── Cookie session and CSRF ────────────────────────────────────────────

    @Test
    void theSessionCookieAuthenticatesAndMeReturnsTheRole() throws Exception {
        mvc.perform(get("/auth/me").cookie(session("manager", RoleEnum.ROLE_MANAGER)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("manager"))
                .andExpect(jsonPath("$.role").value("ROLE_MANAGER"))
                .andExpect(jsonPath("$.expiresAt").exists());
    }

    @Test
    void meWithoutASessionIsUnauthorized() throws Exception {
        mvc.perform(get("/auth/me")).andExpect(status().isUnauthorized());
    }

    @Test
    void aCookieAuthenticatedWriteWithoutCsrfTokenIsRejected() throws Exception {
        mvc.perform(delete("/promotions/7").cookie(session("manager", RoleEnum.ROLE_MANAGER)))
                .andExpect(status().isForbidden());
    }

    @Test
    void aCookieAuthenticatedWriteWithCsrfTokenIsAccepted() throws Exception {
        // What Angular does: read the XSRF-TOKEN cookie and echo it in a header.
        Cookie xsrf = mvc.perform(get("/promotions/1")).andReturn().getResponse().getCookie("XSRF-TOKEN");

        mvc.perform(delete("/promotions/7")
                        .cookie(session("manager", RoleEnum.ROLE_MANAGER), xsrf)
                        .header("X-XSRF-TOKEN", xsrf.getValue()))
                .andExpect(status().isNoContent());
    }

    @Test
    void bearerClientsDoNotNeedACsrfToken() throws Exception {
        mvc.perform(delete("/promotions/7").header("Authorization", bearer("manager", RoleEnum.ROLE_MANAGER)))
                .andExpect(status().isNoContent());
    }

    @Test
    void getRequestsHandOutTheCsrfCookie() throws Exception {
        mvc.perform(get("/promotions/1"))
                .andExpect(cookie().exists("XSRF-TOKEN"));
    }

    @Test
    void logoutClearsTheCookie() throws Exception {
        mvc.perform(post("/auth/logout"))
                .andExpect(status().isNoContent())
                .andExpect(cookie().maxAge("EMH_SESSION", 0));
    }

    @Test
    void unauthorizedResponsesDoNotTriggerTheBrowserLoginPopup() throws Exception {
        mvc.perform(get("/promotions"))
                .andExpect(status().isUnauthorized())
                .andExpect(header().doesNotExist("WWW-Authenticate"));
    }
}
