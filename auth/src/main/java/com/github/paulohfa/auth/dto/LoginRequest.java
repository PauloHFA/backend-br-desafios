package com.github.paulohfa.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "O usuario (username) e obrigatorio")
        String username,

        @NotBlank(message = "A senha (password) e obrigatoria")
        String password
) {
}
