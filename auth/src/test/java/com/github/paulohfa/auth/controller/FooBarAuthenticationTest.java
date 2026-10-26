package com.github.paulohfa.auth.controller;

import com.github.paulohfa.auth.security.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FooBarAuthenticationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Test
    @DisplayName("Deve retornar 204 No Content quando o token de autorizacao for valido")
    void shouldReturn204WhenTokenIsValid() throws Exception {
        String validToken = jwtService.generateToken("test-user");

        mockMvc.perform(get("/foo-bar")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar 204 No Content quando o token for enviado sem o prefixo Bearer")
    void shouldReturn204WhenTokenIsValidWithoutBearerPrefix() throws Exception {
        String validToken = jwtService.generateToken("test-user");

        mockMvc.perform(get("/foo-bar")
                        .header(HttpHeaders.AUTHORIZATION, validToken))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar 401 Unauthorized quando o header Authorization estiver ausente")
    void shouldReturn401WhenHeaderIsMissing() throws Exception {
        mockMvc.perform(get("/foo-bar"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("Token de acesso invalido ou ausente."));
    }

    @Test
    @DisplayName("Deve retornar 401 Unauthorized quando o token for invalido (ex: vYQIYxOpyfr==)")
    void shouldReturn401WhenTokenIsInvalid() throws Exception {
        mockMvc.perform(get("/foo-bar")
                        .header(HttpHeaders.AUTHORIZATION, "vYQIYxOpyfr=="))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("Token de acesso invalido ou ausente."));
    }

    @Test
    @DisplayName("Deve retornar 401 Unauthorized quando o token estiver expirado")
    void shouldReturn401WhenTokenIsExpired() throws Exception {
        String expiredToken = jwtService.generateToken("expired-user", Duration.ofSeconds(-10));

        mockMvc.perform(get("/foo-bar")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + expiredToken))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    @DisplayName("Deve permitir acesso sem token a rotas publicas como /api/public/ping")
    void shouldAllowAccessToPublicRouteWithoutToken() throws Exception {
        mockMvc.perform(get("/api/public/ping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("pong"));
    }

    @Test
    @DisplayName("Deve emitir token com sucesso no endpoint de login")
    void shouldGenerateTokenOnLogin() throws Exception {
        String loginJson = "{\"username\": \"admin\", \"password\": \"admin123\"}";

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.expiresInMinutes").value(60));
    }
}
