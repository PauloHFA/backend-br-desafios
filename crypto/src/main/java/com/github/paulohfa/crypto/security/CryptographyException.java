package com.github.paulohfa.crypto.security;

public class CryptographyException extends RuntimeException {

    public CryptographyException(String message) {
        super(message);
    }

    public CryptographyException(String message, Throwable cause) {
        super(message, cause);
    }
}

