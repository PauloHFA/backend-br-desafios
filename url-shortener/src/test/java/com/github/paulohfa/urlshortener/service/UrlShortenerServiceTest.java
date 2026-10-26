package com.github.paulohfa.urlshortener.service;

import com.github.paulohfa.urlshortener.domain.model.ShortUrl;
import com.github.paulohfa.urlshortener.domain.repository.ShortUrlRepository;
import com.github.paulohfa.urlshortener.dto.ShortenUrlRequest;
import com.github.paulohfa.urlshortener.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UrlShortenerServiceTest {

    private ShortUrlRepository repository;
    private UrlShortenerService service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(ShortUrlRepository.class);
        service = new UrlShortenerService(repository, 7, 6);
    }

    @Test
    @DisplayName("O codigo gerado deve ter entre 5 e 10 caracteres alfanumericos")
    void shouldGenerateValidAlphanumericCode() {
        when(repository.existsByShortCode(any())).thenReturn(false);

        String code = service.generateUniqueShortCode();

        assertNotNull(code);
        assertTrue(code.length() >= 5 && code.length() <= 10);
        assertTrue(code.matches("^[a-zA-Z0-9]+$"));
    }

    @Test
    @DisplayName("Deve encurtar a URL e salvar no repositorio")
    void shouldShortenAndPersist() {
        when(repository.existsByShortCode(any())).thenReturn(false);
        when(repository.save(any(ShortUrl.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ShortenUrlRequest request = new ShortenUrlRequest("https://backendbrasil.com.br");
        ShortUrl result = service.shorten(request);

        assertNotNull(result);
        assertEquals("https://backendbrasil.com.br", result.getOriginalUrl());
        assertNotNull(result.getShortCode());
        assertFalse(result.isExpired());
        verify(repository, times(1)).save(any(ShortUrl.class));
    }

    @Test
    @DisplayName("Deve lancar ResourceNotFoundException quando o codigo nao existir")
    void shouldThrowNotFoundForNonExistingCode() {
        when(repository.findByShortCode("inexistente")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.resolveAndIncrement("inexistente"));
    }

    @Test
    @DisplayName("Deve lancar ResourceNotFoundException quando a URL estiver expirada")
    void shouldThrowNotFoundForExpiredUrl() {
        Instant past = Instant.now().minusSeconds(3600);
        ShortUrl expired = new ShortUrl("ABCDE", "https://example.com", past);

        when(repository.findByShortCode("ABCDE")).thenReturn(Optional.of(expired));

        assertThrows(ResourceNotFoundException.class, () -> service.resolveAndIncrement("ABCDE"));
    }
}
