package com.github.paulohfa.loans.service;

import com.github.paulohfa.loans.domain.enums.LoanType;
import com.github.paulohfa.loans.dto.CustomerLoansRequest;
import com.github.paulohfa.loans.dto.CustomerLoansResponse;
import com.github.paulohfa.loans.dto.LoanDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoanServiceTest {

    private LoanService loanService;

    @BeforeEach
    void setUp() {
        loanService = new LoanService();
    }

    @Test
    @DisplayName("Salario <= 3000 deve conceder emprestimo Pessoal e Com Garantia para qualquer idade e estado")
    void shouldGrantPersonalAndGuaranteedForIncomeUpTo3000() {
        CustomerLoansRequest request = new CustomerLoansRequest(
                45, "111.222.333-44", "Carlos Silva", BigDecimal.valueOf(2500), "MG"
        );

        CustomerLoansResponse response = loanService.evaluateLoans(request);

        assertEquals("Carlos Silva", response.customer());
        assertEquals(2, response.loans().size());
        assertTrue(response.loans().stream().anyMatch(l -> l.type() == LoanType.PERSONAL && l.interestRate() == 4));
        assertTrue(response.loans().stream().anyMatch(l -> l.type() == LoanType.GUARANTEED && l.interestRate() == 3));
    }

    @Test
    @DisplayName("Salario entre 3000 e 5000 com idade < 30 e residindo em SP deve conceder Pessoal e Com Garantia")
    void shouldGrantPersonalAndGuaranteedForUnder30InSP() {
        CustomerLoansRequest request = new CustomerLoansRequest(
                25, "222.333.444-55", "Ana Souza", BigDecimal.valueOf(4000), "SP"
        );

        CustomerLoansResponse response = loanService.evaluateLoans(request);

        assertEquals(2, response.loans().size());
        assertTrue(response.loans().stream().anyMatch(l -> l.type() == LoanType.PERSONAL));
        assertTrue(response.loans().stream().anyMatch(l -> l.type() == LoanType.GUARANTEED));
    }

    @Test
    @DisplayName("Salario entre 3000 e 5000 mas com idade >= 30 nao deve conceder emprestimos")
    void shouldNotGrantWhenAgeIs30OrMore() {
        CustomerLoansRequest request = new CustomerLoansRequest(
                30, "333.444.555-66", "Roberto Santos", BigDecimal.valueOf(4000), "SP"
        );

        CustomerLoansResponse response = loanService.evaluateLoans(request);

        assertTrue(response.loans().isEmpty());
    }

    @Test
    @DisplayName("Salario entre 3000 e 5000 mas fora de SP nao deve conceder emprestimos")
    void shouldNotGrantWhenLocationIsNotSP() {
        CustomerLoansRequest request = new CustomerLoansRequest(
                22, "444.555.666-77", "Mariana Lima", BigDecimal.valueOf(4000), "RJ"
        );

        CustomerLoansResponse response = loanService.evaluateLoans(request);

        assertTrue(response.loans().isEmpty());
    }

    @Test
    @DisplayName("Cenario do PROBLEM.md: Renda 7000, idade 26, SP concede Pessoal, Garantia e Consignado")
    void shouldGrantAllThreeLoansForExampleInProblemMd() {
        CustomerLoansRequest request = new CustomerLoansRequest(
                26, "275.484.389-23", "Vuxaywua Zukiagou", BigDecimal.valueOf(7000.00), "SP"
        );

        CustomerLoansResponse response = loanService.evaluateLoans(request);

        assertEquals("Vuxaywua Zukiagou", response.customer());
        assertEquals(3, response.loans().size());
        List<LoanType> types = response.loans().stream().map(LoanDto::type).toList();
        assertTrue(types.contains(LoanType.PERSONAL));
        assertTrue(types.contains(LoanType.GUARANTEED));
        assertTrue(types.contains(LoanType.CONSIGNMENT));
    }

    @Test
    @DisplayName("Salario >= 5000 fora de SP e idade >= 30 concede apenas Consignado")
    void shouldGrantOnlyConsignmentForHighIncomeOutsideSP() {
        CustomerLoansRequest request = new CustomerLoansRequest(
                35, "555.666.777-88", "Felipe Alves", BigDecimal.valueOf(6000), "PR"
        );

        CustomerLoansResponse response = loanService.evaluateLoans(request);

        assertEquals(1, response.loans().size());
        assertEquals(LoanType.CONSIGNMENT, response.loans().get(0).type());
        assertEquals(2, response.loans().get(0).interestRate());
    }
}
