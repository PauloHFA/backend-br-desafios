package com.github.paulohfa.auth.controller;

import com.github.paulohfa.auth.dto.LoginRequest;
import com.github.paulohfa.auth.dto.LoginResponse;
import com.github.paulohfa.auth.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        // Autenticacao de exemplo / mock
        if ("admin".equalsIgnoreCase(request.username()) && "admin123".equals(request.password())) {
            String token = jwtService.generateToken(request.username());
            return ResponseEntity.ok(LoginResponse.of(token, 60));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Credenciais invalidas"));
    }
}
