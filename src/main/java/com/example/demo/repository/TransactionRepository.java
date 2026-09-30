package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    // Velocity checks: Retrieve recent transactions for a specific user ID through the payment method association
    List<Transaction> findByPaymentMethodAccountUserIdAndTimestampAfter(UUID userId, LocalDateTime since);

    // Card-testing defense: Find attempts across the network for a specific card token string
    List<Transaction> findByPaymentMethodCardTokenAndTimestampAfter(String cardToken, LocalDateTime since);

    // Bot/Scripting defense: Count high-frequency attempts from a single session ID
    long countBySessionSessionIdAndTimestampAfter(Long sessionId, LocalDateTime since);
}