package com.example.demo.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Decision;

@Repository
public interface DecisionRepository extends JpaRepository<Decision, UUID> {

    // Optional query method to find a decision by its associated transaction ID
    Optional<Decision> findByTransactionTransactionId(UUID transactionId);
}