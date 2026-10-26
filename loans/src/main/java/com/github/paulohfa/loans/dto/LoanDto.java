package com.github.paulohfa.loans.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.paulohfa.loans.domain.enums.LoanType;

public record LoanDto(
        LoanType type,

        @JsonProperty("interest_rate")
        int interestRate
) {
    public static LoanDto of(LoanType type) {
        return new LoanDto(type, type.getInterestRate());
    }
}
