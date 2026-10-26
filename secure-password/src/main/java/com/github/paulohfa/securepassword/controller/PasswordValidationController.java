package com.github.paulohfa.securepassword.controller;

import com.github.paulohfa.securepassword.dto.PasswordValidationFailureResponse;
import com.github.paulohfa.securepassword.dto.ValidatePasswordRequest;
import com.github.paulohfa.securepassword.service.PasswordValidatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PasswordValidationController {

    private final PasswordValidatorService validatorService;

    public PasswordValidationController(PasswordValidatorService validatorService) {
        this.validatorService = validatorService;
    }

    @PostMapping("/validate-password")
    public ResponseEntity<?> validatePassword(@RequestBody ValidatePasswordRequest request) {
        List<String> failures = validatorService.validate(request != null ? request.password() : null);

        if (failures.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.badRequest().body(PasswordValidationFailureResponse.of(failures));
    }
}
