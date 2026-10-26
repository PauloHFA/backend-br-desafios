package com.github.paulohfa.loans.service;

import com.github.paulohfa.loans.domain.enums.LoanType;
import com.github.paulohfa.loans.dto.CustomerLoansRequest;
import com.github.paulohfa.loans.dto.CustomerLoansResponse;
import com.github.paulohfa.loans.dto.LoanDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanService {

    private static final BigDecimal THREE_THOUSAND = BigDecimal.valueOf(3000);
    private static final BigDecimal FIVE_THOUSAND = BigDecimal.valueOf(5000);

    public CustomerLoansResponse evaluateLoans(CustomerLoansRequest request) {
        List<LoanDto> availableLoans = new ArrayList<>();

        if (isPersonalLoanAvailable(request.income(), request.age(), request.location())) {
            availableLoans.add(LoanDto.of(LoanType.PERSONAL));
        }

        if (isGuaranteedLoanAvailable(request.income(), request.age(), request.location())) {
            availableLoans.add(LoanDto.of(LoanType.GUARANTEED));
        }

        if (isConsignmentLoanAvailable(request.income())) {
            availableLoans.add(LoanDto.of(LoanType.CONSIGNMENT));
        }

        return new CustomerLoansResponse(request.name(), availableLoans);
    }

    /**
     * Regras para Emprestimo Pessoal (4%):
     * - Salario <= 3000
     * - Salario entre 3000 e 5000 (ou superior), caso o cliente tenha menos de 30 anos e resida em SP.
     */
    public boolean isPersonalLoanAvailable(BigDecimal income, int age, String location) {
        if (income.compareTo(THREE_THOUSAND) <= 0) {
            return true;
        }

        return age < 30 && "SP".equalsIgnoreCase(location);
    }

    /**
     * Regras para Emprestimo com Garantia (3%):
     * - Salario <= 3000
     * - Salario entre 3000 e 5000 (ou superior), caso o cliente tenha menos de 30 anos e resida em SP.
     */
    public boolean isGuaranteedLoanAvailable(BigDecimal income, int age, String location) {
        if (income.compareTo(THREE_THOUSAND) <= 0) {
            return true;
        }

        return age < 30 && "SP".equalsIgnoreCase(location);
    }

    /**
     * Regras para Emprestimo Consignado (2%):
     * - Salario >= 5000
     */
    public boolean isConsignmentLoanAvailable(BigDecimal income) {
        return income.compareTo(FIVE_THOUSAND) >= 0;
    }
}
