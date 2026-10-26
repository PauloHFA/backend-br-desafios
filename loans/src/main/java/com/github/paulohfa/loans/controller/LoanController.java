package com.github.paulohfa.loans.controller;

import com.github.paulohfa.loans.dto.CustomerLoansRequest;
import com.github.paulohfa.loans.dto.CustomerLoansResponse;
import com.github.paulohfa.loans.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer-loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<CustomerLoansResponse> evaluateLoans(@RequestBody @Valid CustomerLoansRequest request) {
        CustomerLoansResponse response = loanService.evaluateLoans(request);
        return ResponseEntity.ok(response);
    }
}
