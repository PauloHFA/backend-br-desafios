package com.github.paulohfa.auth.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService("SecretTestKeyBackendBrChallenges2026", 60);
    }

    @Test
    @DisplayName("Deve gerar token e validar com sucesso")
    void shouldGenerateAndValidateToken() {
        String username = "paulo";
        String token = jwtService.generateToken(username);

        assertNotNull(token);
        assertFalse(token.isBlank());

        Optional<String> subject = jwtService.validateAndGetSubject(token);
        assertTrue(subject.isPresent());
        assertEquals(username, subject.get());
    }

    @Test
    @DisplayName("Deve validar token mesmo com prefixo 'Bearer '")
    void shouldValidateTokenWithBearerPrefix() {
        String token = jwtService.generateToken("admin");
        Optional<String> subject = jwtService.validateAndGetSubject("Bearer " + token);

        assertTrue(subject.isPresent());
        assertEquals("admin", subject.get());
    }

    @Test
    @DisplayName("Deve rejeitar token expirado")
    void shouldRejectExpiredToken() {
        // Token gerado já no passado (expirado há 5 segundos)
        String expiredToken = jwtService.generateToken("user", Duration.ofSeconds(-5));

        Optional<String> subject = jwtService.validateAndGetSubject(expiredToken);
        assertTrue(subject.isEmpty());
    }

    @Test
    @DisplayName("Deve rejeitar token com assinatura adulterada ou invalida")
    void shouldRejectInvalidToken() {
        Optional<String> subject = jwtService.validateAndGetSubject("vYQIYxOpyfr==");
        assertTrue(subject.isEmpty());
    }

    @Test
    @DisplayName("Deve retornar vazio para token nulo ou em branco")
    void shouldReturnEmptyForNullOrBlankToken() {
        assertTrue(jwtService.validateAndGetSubject(null).isEmpty());
        assertTrue(jwtService.validateAndGetSubject("   ").isEmpty());
    }
}
