package com.github.paulohfa.pointsofinterest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PointOfInterestRequest(
        @NotBlank(message = "O nome do ponto de interesse (name) e obrigatorio")
        String name,

        @NotNull(message = "A coordenada X (x) e obrigatoria")
        @Min(value = 0, message = "A coordenada X deve ser um numero inteiro nao negativo (>= 0)")
        Integer x,

        @NotNull(message = "A coordenada Y (y) e obrigatoria")
        @Min(value = 0, message = "A coordenada Y deve ser um numero inteiro nao negativo (>= 0)")
        Integer y
) {
}
