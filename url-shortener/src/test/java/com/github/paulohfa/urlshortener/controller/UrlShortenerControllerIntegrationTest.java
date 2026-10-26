package com.github.paulohfa.urlshortener.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UrlShortenerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Ciclo completo: deve encurtar URL e redirecionar com status 302")
    void shouldShortenAndRedirectSuccessfully() throws Exception {
        String originalUrl = "https://backendbrasil.com.br";
        String payload = "{\"url\": \"" + originalUrl + "\"}";

        // 1. Encurtar URL
        MvcResult mvcResult = mockMvc.perform(post("/shorten-url")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").isString())
                .andReturn();

        JsonNode responseJson = objectMapper.readTree(mvcResult.getResponse().getContentAsString());
        String fullShortUrl = responseJson.get("url").asText();

        // Extrai o codigo final da URL (ex: http://localhost/ABCDEF -> ABCDEF)
        String code = fullShortUrl.substring(fullShortUrl.lastIndexOf('/') + 1);

        // 2. Fazer requisicao ao endpoint encurtado e verificar o redirecionamento (302 Found)
        mockMvc.perform(get("/" + code))
                .andExpect(status().isFound())
                .andExpect(header().string(HttpHeaders.LOCATION, originalUrl));
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found para codigo inexistente")
    void shouldReturn404ForNonExistentCode() throws Exception {
        mockMvc.perform(get("/naoExiste99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request para formato de URL invalido")
    void shouldReturn400ForInvalidUrl() throws Exception {
        String invalidPayload = "{\"url\": \"isto-nao-e-uma-url\"}";

        mockMvc.perform(post("/shorten-url")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields.url").exists());
    }
}
