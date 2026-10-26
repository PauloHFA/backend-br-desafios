package com.github.paulohfa.crypto.service;

import com.github.paulohfa.crypto.domain.model.Operation;
import com.github.paulohfa.crypto.domain.repository.OperationRepository;
import com.github.paulohfa.crypto.dto.OperationRequest;
import com.github.paulohfa.crypto.dto.OperationResponse;
import com.github.paulohfa.crypto.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OperationService {

    private final OperationRepository repository;

    public OperationService(OperationRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public OperationResponse create(OperationRequest request) {
        Operation operation = new Operation(
                request.userDocument(),
                request.creditCardToken(),
                request.value()
        );
        Operation saved = repository.save(operation);
        return OperationResponse.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public OperationResponse findById(Long id) {
        Operation operation = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Operacao com ID " + id + " nao encontrada"));
        return OperationResponse.fromEntity(operation);
    }

    @Transactional(readOnly = true)
    public Page<OperationResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(OperationResponse::fromEntity);
    }

    @Transactional
    public OperationResponse update(Long id, OperationRequest request) {
        Operation operation = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Operacao com ID " + id + " nao encontrada"));

        operation.setUserDocument(request.userDocument());
        operation.setCreditCardToken(request.creditCardToken());
        operation.setValue(request.value());

        Operation updated = repository.save(operation);
        return OperationResponse.fromEntity(updated);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Operacao com ID " + id + " nao encontrada");
        }
        repository.deleteById(id);
    }
}
