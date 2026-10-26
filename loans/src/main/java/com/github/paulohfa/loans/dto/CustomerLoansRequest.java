package com.github.paulohfa.loans.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record CustomerLoansRequest(
        @NotNull(message = "A idade (age) e obrigatoria")
        @Min(value = 18, message = "A idade deve ser igual ou superior a 18 anos")
        Integer age,

        @NotBlank(message = "O CPF (cpf) e obrigatorio")
        String cpf,

        @NotBlank(message = "O nome (name) e obrigatorio")
        String name,

        @NotNull(message = "A renda (income) e obrigatoria")
        @PositiveOrZero(message = "A renda deve ser maior ou igual a zero")
        BigDecimal income,

        @NotBlank(message = "A localizacao (location) e obrigatoria")
        String location
) {
}
