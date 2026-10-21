package org.unibl.etf.emobility_hub_promotions.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.unibl.etf.emobility_hub_promotions.beans.UserBean;

import static org.assertj.core.api.Assertions.assertThat;

class AuthServiceTest {
    FakeApi api;
    AuthService service;

    @BeforeEach
    void start() throws Exception {
        api = new FakeApi();
        service = new AuthService(api.url("/auth/login"));
    }

    @AfterEach
    void stop() {
        api.close();
    }

    @Test
    void returnsTokenUsernameAndRoleFromTheLoginResponse() {
        api.respond(200, "{\"token\":\"jwt-123\",\"username\":\"manager\",\"role\":\"ROLE_MANAGER\",\"expiresAt\":\"2030-01-01T00:00:00Z\"}");

        AuthService.LoginResult result = service.login("manager", "manager");

        assertThat(result.getToken()).isEqualTo("jwt-123");
        assertThat(result.getUsername()).isEqualTo("manager");
        assertThat(result.getRole()).isEqualTo("ROLE_MANAGER");
        assertThat(api.last().method()).isEqualTo("POST");
    }

    @Test
    void reportsTheRoleOfANonManagerSoTheLoginPageCanRefuseIt() {
        api.respond(200, "{\"token\":\"t\",\"username\":\"operator\",\"role\":\"ROLE_OPERATOR\"}");

        UserBean user = new UserBean();
        AuthService.LoginResult result = service.login("operator", "operator");
        user.signIn(result.getUsername(), result.getRole(), result.getToken());

        assertThat(result.getRole()).isEqualTo("ROLE_OPERATOR");
        assertThat(user.isManager()).isFalse();
    }

    @Test
    void sendsValidJsonEvenWhenThePasswordHasQuotes() throws Exception {
        api.respond(200, "{\"token\":\"t\"}");

        service.login("manager", "pa\"ss\\word");

        JsonNode sent = new ObjectMapper().readTree(api.last().body());
        assertThat(sent.get("username").asText()).isEqualTo("manager");
        assertThat(sent.get("password").asText()).isEqualTo("pa\"ss\\word");
    }

    @Test
    void returnsNullWhenTheCredentialsAreWrong() {
        api.respond(401, "Invalid username or password");

        assertThat(service.login("manager", "wrong")).isNull();
    }
}
