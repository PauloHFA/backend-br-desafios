package com.github.paulohfa.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record ShortenUrlRequest(
        @NotBlank(message = "A URL (url) e obrigatoria")
        @URL(message = "A URL informada deve possuir formato valido (ex: https://exemplo.com)")
        String url
) {
}
