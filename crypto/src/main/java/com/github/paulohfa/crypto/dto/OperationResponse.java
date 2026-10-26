package com.github.paulohfa.crypto.dto;

import com.github.paulohfa.crypto.domain.model.Operation;

public record OperationResponse(
        Long id,
        String userDocument,
        String creditCardToken,
        Long value
) {
    public static OperationResponse fromEntity(Operation operation) {
        return new OperationResponse(
                operation.getId(),
                operation.getUserDocument(),
                operation.getCreditCardToken(),
                operation.getValue()
        );
    }
}
