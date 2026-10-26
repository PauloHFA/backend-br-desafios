package com.github.paulohfa.crypto.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.paulohfa.crypto.dto.OperationRequest;
import com.github.paulohfa.crypto.security.CryptoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OperationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CryptoService cryptoService;

    @Test
    @DisplayName("Deve salvar operacao com criptografia transparente no banco e descriptografar na API")
    void shouldPersistEncryptedInDatabaseAndReturnDecryptedInApi() throws Exception {
        String originalDoc = "123.456.789-00";
        String originalToken = "tok_visa_4242_super_secret";
        Long originalValue = 15000L;

        OperationRequest request = new OperationRequest(originalDoc, originalToken, originalValue);

        // 1. Envia via POST na API
        MvcResult postResult = mockMvc.perform(post("/api/operations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.userDocument").value(originalDoc))
                .andExpect(jsonPath("$.creditCardToken").value(originalToken))
                .andExpect(jsonPath("$.value").value(originalValue))
                .andReturn();

        JsonNode jsonNode = objectMapper.readTree(postResult.getResponse().getContentAsString());
        long createdId = jsonNode.get("id").asLong();

        // 2. Consulta direta via SQL no banco de dados para comprovar que esta CIFRADO
        Map<String, Object> rawDbRow = jdbcTemplate.queryForMap(
                "SELECT user_document, credit_card_token, operation_value FROM tb_operations WHERE id = ?",
                createdId
        );

        String rawDbUserDocument = (String) rawDbRow.get("USER_DOCUMENT");
        String rawDbCreditCardToken = (String) rawDbRow.get("CREDIT_CARD_TOKEN");
        Number rawDbValue = (Number) rawDbRow.get("OPERATION_VALUE");

        // Verifica que o dado gravado no banco NÃO é o texto puro
        assertThat(rawDbUserDocument).isNotEqualTo(originalDoc);
        assertThat(rawDbCreditCardToken).isNotEqualTo(originalToken);
        assertThat(rawDbValue.longValue()).isEqualTo(originalValue);

        // Verifica que ao decifrar a coluna bruta do banco com o CryptoService recuperamos o valor original
        assertThat(cryptoService.decrypt(rawDbUserDocument)).isEqualTo(originalDoc);
        assertThat(cryptoService.decrypt(rawDbCreditCardToken)).isEqualTo(originalToken);

        // 3. Consulta via GET na API para comprovar que a leitura é transparente
        mockMvc.perform(get("/api/operations/{id}", createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdId))
                .andExpect(jsonPath("$.userDocument").value(originalDoc))
                .andExpect(jsonPath("$.creditCardToken").value(originalToken))
                .andExpect(jsonPath("$.value").value(originalValue));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request ao enviar dados invalidos")
    void shouldReturn400WhenValidationFails() throws Exception {
        OperationRequest invalidRequest = new OperationRequest("", "", -100L);

        mockMvc.perform(post("/api/operations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields.userDocument").exists())
                .andExpect(jsonPath("$.fields.creditCardToken").exists())
                .andExpect(jsonPath("$.fields.value").exists());
    }

    @Test
    @DisplayName("Deve retornar 404 quando o ID pesquisado nao existir")
    void shouldReturn404WhenNotFound() throws Exception {
        mockMvc.perform(get("/api/operations/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"));
    }
}

