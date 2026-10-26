package com.github.paulohfa.securepassword.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class PasswordValidatorService {

    private static final int MIN_LENGTH = 8;
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern LOWERCASE_PATTERN = Pattern.compile("[a-z]");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("[0-9]");
    // Aceita qualquer caractere especial tipico: símbolos de pontuação e especiais ASCII/Unicode
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?`~]");

    /**
     * Valida a senha contra todas as regras do desafio.
     *
     * @param password senha a ser validada
     * @return Lista com as descricoes dos criterios nao atendidos. Se a lista estiver vazia, a senha e valida.
     */
    public List<String> validate(String password) {
        List<String> failures = new ArrayList<>();

        if (password == null || password.length() < MIN_LENGTH) {
            failures.add("A senha deve possuir pelo menos 8 caracteres.");
        }

        if (password == null || !UPPERCASE_PATTERN.matcher(password).find()) {
            failures.add("A senha deve conter pelo menos uma letra maiúscula.");
        }

        if (password == null || !LOWERCASE_PATTERN.matcher(password).find()) {
            failures.add("A senha deve conter pelo menos uma letra minúscula.");
        }

        if (password == null || !DIGIT_PATTERN.matcher(password).find()) {
            failures.add("A senha deve conter pelo menos um dígito numérico.");
        }

        if (password == null || !SPECIAL_CHAR_PATTERN.matcher(password).find()) {
            failures.add("A senha deve conter pelo menos um caractere especial (ex: ! @ # $ %).");
        }

        return failures;
    }
}
