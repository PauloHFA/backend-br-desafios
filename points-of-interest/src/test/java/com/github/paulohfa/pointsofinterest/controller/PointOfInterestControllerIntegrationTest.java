package com.github.paulohfa.pointsofinterest.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PointOfInterestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /points-of-interest/nearby - Deve retornar exatamente os 4 POIs proximos do exemplo (x=20, y=10, dmax=10)")
    void shouldReturnCorrectNearbyPointsFromProblemExample() throws Exception {
        // Exemplo oficial do desafio:
        // Referencia: (20, 10), dmax = 10
        // Esperados: Lanchonete (d ~ 7.28), Joalheria (d ~ 5.39), Pub (d ~ 8.25), Supermercado (d = 5.0)
        mockMvc.perform(get("/points-of-interest/nearby")
                        .param("x", "20")
                        .param("y", "10")
                        .param("dmax", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Lanchonete", "Joalheria", "Pub", "Supermercado")))
                .andExpect(jsonPath("$[*].name", not(hasItem("Posto"))))
                .andExpect(jsonPath("$[*].name", not(hasItem("Floricultura"))))
                .andExpect(jsonPath("$[*].name", not(hasItem("Churrascaria"))));
    }

    @Test
    @DisplayName("POST /points-of-interest - Deve cadastrar um novo ponto com sucesso (201 Created)")
    void shouldCreateNewPointOfInterest() throws Exception {
        String payload = """
                {
                  "name": "Academia Central",
                  "x": 10,
                  "y": 15
                }
                """;

        mockMvc.perform(post("/points-of-interest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Academia Central"))
                .andExpect(jsonPath("$.x").value(10))
                .andExpect(jsonPath("$.y").value(15));
    }

    @Test
    @DisplayName("POST /points-of-interest - Deve rejeitar coordenadas negativas com 400 Bad Request")
    void shouldRejectNegativeCoordinates() throws Exception {
        String payload = """
                {
                  "name": "Ponto Invalido",
                  "x": -5,
                  "y": 10
                }
                """;

        mockMvc.perform(post("/points-of-interest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields.x").exists());
    }

    @Test
    @DisplayName("GET /points-of-interest/nearby - Deve retornar 400 Bad Request se dmax for negativo")
    void shouldRejectNegativeDmax() throws Exception {
        mockMvc.perform(get("/points-of-interest/nearby")
                        .param("x", "20")
                        .param("y", "10")
                        .param("dmax", "-1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("A distancia maxima (dmax) nao pode ser negativa."));
    }
}
