package com.github.paulohfa.securepassword.dto;

import java.time.Instant;
import java.util.List;

public record PasswordValidationFailureResponse(
        String timestamp,
        int status,
        String error,
        String message,
        List<String> failures
) {
    public static PasswordValidationFailureResponse of(List<String> failures) {
        return new PasswordValidationFailureResponse(
                Instant.now().toString(),
                400,
                "Bad Request",
                "A senha fornecida nao atende a todos os criterios de seguranca.",
                failures
        );
    }
}
