package com.github.paulohfa.securepassword.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PasswordValidatorServiceTest {

    private PasswordValidatorService validator;

    @BeforeEach
    void setUp() {
        validator = new PasswordValidatorService();
    }

    @Test
    @DisplayName("Deve validar com sucesso a senha de exemplo do PROBLEM.md (vYQIYxO&p$yfI^r)")
    void shouldValidateValidPassword() {
        List<String> failures = validator.validate("vYQIYxO&p$yfI^r");
        assertTrue(failures.isEmpty());
    }

    @Test
    @DisplayName("Deve falhar quando tiver menos de 8 caracteres")
    void shouldFailWhenLengthIsLessThan8() {
        List<String> failures = validator.validate("Ab1@xyz"); // 7 caracteres
        assertEquals(1, failures.size());
        assertTrue(failures.get(0).contains("8 caracteres"));
    }

    @Test
    @DisplayName("Deve falhar quando nao contiver letra maiuscula")
    void shouldFailWhenMissingUppercase() {
        List<String> failures = validator.validate("abcdef1@gh");
        assertEquals(1, failures.size());
        assertTrue(failures.get(0).contains("letra maiúscula"));
    }

    @Test
    @DisplayName("Deve falhar quando nao contiver letra minuscula")
    void shouldFailWhenMissingLowercase() {
        List<String> failures = validator.validate("ABCDEF1@GH");
        assertEquals(1, failures.size());
        assertTrue(failures.get(0).contains("letra minúscula"));
    }

    @Test
    @DisplayName("Deve falhar quando nao contiver digito numerico")
    void shouldFailWhenMissingDigit() {
        List<String> failures = validator.validate("Abcdefgh@!");
        assertEquals(1, failures.size());
        assertTrue(failures.get(0).contains("dígito numérico"));
    }

    @Test
    @DisplayName("Deve falhar quando nao contiver caractere especial")
    void shouldFailWhenMissingSpecialChar() {
        List<String> failures = validator.validate("Abcdefgh12");
        assertEquals(1, failures.size());
        assertTrue(failures.get(0).contains("caractere especial"));
    }

    @Test
    @DisplayName("Deve acusar todas as 5 falhas quando a senha for nula")
    void shouldFailAllCriteriaForNullPassword() {
        List<String> failures = validator.validate(null);
        assertEquals(5, failures.size());
    }
}
