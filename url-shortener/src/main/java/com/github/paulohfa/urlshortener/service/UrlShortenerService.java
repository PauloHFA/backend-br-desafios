package com.github.paulohfa.urlshortener.service;

import com.github.paulohfa.urlshortener.domain.model.ShortUrl;
import com.github.paulohfa.urlshortener.domain.repository.ShortUrlRepository;
import com.github.paulohfa.urlshortener.dto.ShortenUrlRequest;
import com.github.paulohfa.urlshortener.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;

@Service
public class UrlShortenerService {

    private static final String ALPHANUMERIC = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final SecureRandom random = new SecureRandom();

    private final ShortUrlRepository repository;
    private final int expirationDays;
    private final int codeLength;

    public UrlShortenerService(
            ShortUrlRepository repository,
            @Value("${app.url-shortener.expiration-days:7}") int expirationDays,
            @Value("${app.url-shortener.code-length:6}") int codeLength
    ) {
        this.repository = repository;
        this.expirationDays = expirationDays;
        this.codeLength = Math.max(5, Math.min(10, codeLength)); // Garante entre 5 e 10
    }

    @Transactional
    public ShortUrl shorten(ShortenUrlRequest request) {
        String uniqueCode = generateUniqueShortCode();
        Instant expiresAt = Instant.now().plus(Duration.ofDays(expirationDays));

        ShortUrl shortUrl = new ShortUrl(uniqueCode, request.url(), expiresAt);
        return repository.save(shortUrl);
    }

    @Transactional
    public String resolveAndIncrement(String shortCode) {
        ShortUrl shortUrl = repository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResourceNotFoundException("URL encurtada '" + shortCode + "' nao encontrada."));

        if (shortUrl.isExpired()) {
            throw new ResourceNotFoundException("A URL encurtada '" + shortCode + "' expirou.");
        }

        shortUrl.incrementAccessCount();
        repository.save(shortUrl);

        return shortUrl.getOriginalUrl();
    }

    public String generateUniqueShortCode() {
        String code;
        int attempts = 0;
        do {
            code = generateRandomAlphanumeric(codeLength);
            attempts++;
            if (attempts > 20) {
                // Caso extremo de colisoes, aumenta o tamanho
                code = generateRandomAlphanumeric(codeLength + 1);
            }
        } while (repository.existsByShortCode(code));

        return code;
    }

    private String generateRandomAlphanumeric(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(ALPHANUMERIC.length());
            sb.append(ALPHANUMERIC.charAt(index));
        }
        return sb.toString();
    }
}
