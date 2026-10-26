package com.github.paulohfa.crypto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OperationRequest(
        @NotBlank(message = "O documento do usuario (userDocument) e obrigatorio")
        String userDocument,

        @NotBlank(message = "O token do cartao (creditCardToken) e obrigatorio")
        String creditCardToken,

        @NotNull(message = "O valor da operacao (value) e obrigatorio")
        @Positive(message = "O valor deve ser maior que zero")
        Long value
) {
}
