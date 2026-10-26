package com.github.paulohfa.securepassword.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PasswordValidationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("POST /validate-password - Deve retornar 204 No Content para senha segura")
    void shouldReturn204ForSecurePassword() throws Exception {
        String payload = """
                {
                  "password": "vYQIYxO&p$yfI^r"
                }
                """;

        mockMvc.perform(post("/validate-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("POST /validate-password - Deve retornar 400 Bad Request com lista de falhas para senha fraca")
    void shouldReturn400WithFailuresForWeakPassword() throws Exception {
        String payload = """
                {
                  "password": "senha"
                }
                """;

        mockMvc.perform(post("/validate-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.failures", hasSize(4))); // Falta: tamanho, maiuscula, numero, especial
    }
}
