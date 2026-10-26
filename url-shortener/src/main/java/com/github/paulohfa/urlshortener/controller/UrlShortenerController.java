package com.github.paulohfa.urlshortener.controller;

import com.github.paulohfa.urlshortener.domain.model.ShortUrl;
import com.github.paulohfa.urlshortener.dto.ShortenUrlRequest;
import com.github.paulohfa.urlshortener.dto.ShortenUrlResponse;
import com.github.paulohfa.urlshortener.service.UrlShortenerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
public class UrlShortenerController {

    private final UrlShortenerService service;

    public UrlShortenerController(UrlShortenerService service) {
        this.service = service;
    }

    @PostMapping("/shorten-url")
    public ResponseEntity<ShortenUrlResponse> shortenUrl(
            @RequestBody @Valid ShortenUrlRequest request,
            HttpServletRequest servletRequest
    ) {
        ShortUrl shortUrl = service.shorten(request);

        String fullShortUrl = ServletUriComponentsBuilder.fromRequestUri(servletRequest)
                .replacePath("/" + shortUrl.getShortCode())
                .replaceQuery(null)
                .toUriString();

        return ResponseEntity.ok(ShortenUrlResponse.of(fullShortUrl));
    }

    @GetMapping("/{code:[a-zA-Z0-9]{5,10}}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        String originalUrl = service.resolveAndIncrement(code);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }
}
