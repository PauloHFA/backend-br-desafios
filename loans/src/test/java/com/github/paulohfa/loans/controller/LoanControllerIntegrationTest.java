package com.github.paulohfa.loans.controller;

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
class LoanControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("POST /customer-loans - Deve retornar as modalidades elegiveis com sucesso (200 OK)")
    void shouldEvaluateLoansSuccessfully() throws Exception {
        String payload = """
                {
                  "age": 26,
                  "cpf": "275.484.389-23",
                  "name": "Vuxaywua Zukiagou",
                  "income": 7000.00,
                  "location": "SP"
                }
                """;

        mockMvc.perform(post("/customer-loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customer").value("Vuxaywua Zukiagou"))
                .andExpect(jsonPath("$.loans", hasSize(3)))
                .andExpect(jsonPath("$.loans[?(@.type == 'PERSONAL')].interest_rate").value(4))
                .andExpect(jsonPath("$.loans[?(@.type == 'GUARANTEED')].interest_rate").value(3))
                .andExpect(jsonPath("$.loans[?(@.type == 'CONSIGNMENT')].interest_rate").value(2));
    }

    @Test
    @DisplayName("POST /customer-loans - Deve retornar 400 Bad Request quando os campos forem invalidos")
    void shouldReturnBadRequestOnInvalidFields() throws Exception {
        String invalidPayload = """
                {
                  "age": 16,
                  "cpf": "",
                  "name": "",
                  "income": -100,
                  "location": ""
                }
                """;

        mockMvc.perform(post("/customer-loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields.age").exists())
                .andExpect(jsonPath("$.fields.cpf").exists())
                .andExpect(jsonPath("$.fields.name").exists())
                .andExpect(jsonPath("$.fields.income").exists())
                .andExpect(jsonPath("$.fields.location").exists());
    }
}
