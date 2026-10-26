package com.github.paulohfa.crypto.domain.repository;

import com.github.paulohfa.crypto.domain.model.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
}
