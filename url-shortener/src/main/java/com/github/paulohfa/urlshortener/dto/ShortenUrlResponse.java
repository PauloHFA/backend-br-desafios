package com.github.paulohfa.urlshortener.dto;

public record ShortenUrlResponse(
        String url
) {
    public static ShortenUrlResponse of(String url) {
        return new ShortenUrlResponse(url);
    }
}
