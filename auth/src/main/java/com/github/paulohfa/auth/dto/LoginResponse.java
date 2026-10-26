package com.github.paulohfa.auth.dto;

public record LoginResponse(
        String token,
        String tokenType,
        long expiresInMinutes
) {
    public static LoginResponse of(String token, long expiresInMinutes) {
        return new LoginResponse(token, "Bearer", expiresInMinutes);
    }
}
