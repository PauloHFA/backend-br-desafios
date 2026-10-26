package com.github.paulohfa.loans.dto;

import java.util.List;

public record CustomerLoansResponse(
        String customer,
        List<LoanDto> loans
) {
}
