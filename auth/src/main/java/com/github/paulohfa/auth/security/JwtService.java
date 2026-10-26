package com.github.paulohfa.auth.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

/**
 * Servico responsavel pela geracao, assinatura e validacao de tokens JWT.
 */
@Service
public class JwtService {

    private static final String ISSUER = "desafios-backend-br-auth";

    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final long defaultExpirationMinutes;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-minutes:60}") long defaultExpirationMinutes
    ) {
        this.algorithm = Algorithm.HMAC256(secret);
        this.verifier = JWT.require(this.algorithm)
                .withIssuer(ISSUER)
                .build();
        this.defaultExpirationMinutes = defaultExpirationMinutes;
    }

    /**
     * Gera um token valido para o usuario especificado.
     */
    public String generateToken(String username) {
        return generateToken(username, Duration.ofMinutes(defaultExpirationMinutes));
    }

    /**
     * Gera um token com duracao customizada (util para cenarios de teste de expiracao).
     */
    public String generateToken(String username, Duration validity) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(validity);

        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(username)
                .withIssuedAt(now)
                .withExpiresAt(expiresAt)
                .sign(algorithm);
    }

    /**
     * Valida o token e retorna o Subject (username) caso seja valido.
     * Retorna Optional.empty() se for invalido, expirado ou com assinatura incorreta.
     */
    public Optional<String> validateAndGetSubject(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }

        // Remove o prefixo "Bearer " caso presente
        String cleanToken = token.trim();
        if (cleanToken.toLowerCase().startsWith("bearer ")) {
            cleanToken = cleanToken.substring(7).trim();
        }

        try {
            DecodedJWT decodedJWT = verifier.verify(cleanToken);
            return Optional.ofNullable(decodedJWT.getSubject());
        } catch (JWTVerificationException ex) {
            return Optional.empty();
        }
    }
}
