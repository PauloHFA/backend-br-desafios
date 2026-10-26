package com.github.paulohfa.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador que expoem o endpoint protegido solicitado no desafio backend-br.
 */
@RestController
public class FooBarController {

    @GetMapping("/foo-bar")
    public ResponseEntity<Void> fooBar() {
        return ResponseEntity.noContent().build();
    }
}
