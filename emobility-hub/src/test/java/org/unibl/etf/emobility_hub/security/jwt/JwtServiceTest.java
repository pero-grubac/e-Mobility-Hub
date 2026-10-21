package org.unibl.etf.emobility_hub.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.unibl.etf.emobility_hub.models.domain.entity.UserEntity;
import org.unibl.etf.emobility_hub.models.domain.value.RoleEnum;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtServiceTest {

    static final String KEY = "413F4428472B4B6250655368566D5970337336763979244226452948404D6351";
    static final String OTHER_KEY = "5A7134743777217A25432A462D4A614E645267556B58703273357638792F423F";

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "jwtSigningKey", KEY);
    }

    static UserEntity user(String username, RoleEnum role) {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword("x");
        user.setRole(role);
        return user;
    }

    @Test
    void generatedTokenCarriesUsernameAndRole() {
        String token = jwtService.generateToken(user("manager", RoleEnum.ROLE_MANAGER));

        assertThat(jwtService.extractUsername(token)).isEqualTo("manager");
        String payload = new String(Base64.getUrlDecoder().decode(token.split("\\.")[1]), StandardCharsets.UTF_8);
        assertThat(payload).contains("\"role\":\"ROLE_MANAGER\"");
    }

    @Test
    void tokenIsValidOnlyForItsOwnUser() {
        String token = jwtService.generateToken(user("admin", RoleEnum.ROLE_ADMIN));

        assertThat(jwtService.isTokenValid(token, user("admin", RoleEnum.ROLE_ADMIN))).isTrue();
        assertThat(jwtService.isTokenValid(token, user("operator", RoleEnum.ROLE_OPERATOR))).isFalse();
    }

    @Test
    void tokenSignedWithAnotherKeyIsRejected() {
        JwtService foreign = new JwtService();
        ReflectionTestUtils.setField(foreign, "jwtSigningKey", OTHER_KEY);
        String forged = foreign.generateToken(user("admin", RoleEnum.ROLE_ADMIN));

        assertThatThrownBy(() -> jwtService.extractUsername(forged)).isInstanceOf(JwtException.class);
    }

    @Test
    void expiredTokenIsRejected() {
        String expired = Jwts.builder()
                .subject("admin")
                .issuedAt(new Date(System.currentTimeMillis() - 10_000))
                .expiration(new Date(System.currentTimeMillis() - 5_000))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(KEY)))
                .compact();

        assertThatThrownBy(() -> jwtService.extractUsername(expired)).isInstanceOf(ExpiredJwtException.class);
    }

    @Test
    void malformedTokenIsRejected() {
        assertThatThrownBy(() -> jwtService.extractUsername("not.a.jwt")).isInstanceOf(JwtException.class);
    }
}
