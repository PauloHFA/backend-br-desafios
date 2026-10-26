package com.github.paulohfa.urlshortener.domain.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
        name = "tb_short_urls",
        indexes = {
                @Index(name = "idx_short_code", columnList = "short_code", unique = true)
        }
)
public class ShortUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "short_code", nullable = false, length = 10, unique = true)
    private String shortCode;

    @Column(name = "original_url", nullable = false, length = 2048)
    private String originalUrl;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "access_count", nullable = false)
    private Long accessCount = 0L;

    public ShortUrl() {
    }

    public ShortUrl(String shortCode, String originalUrl, Instant expiresAt) {
        this.shortCode = shortCode;
        this.originalUrl = originalUrl;
        this.createdAt = Instant.now();
        this.expiresAt = expiresAt;
        this.accessCount = 0L;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    public void incrementAccessCount() {
        this.accessCount++;
    }

    public Long getId() {
        return id;
    }

    public String getShortCode() {
        return shortCode;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public Long getAccessCount() {
        return accessCount;
    }
}
