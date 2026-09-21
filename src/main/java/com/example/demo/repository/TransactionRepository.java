package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    // Velocity checks: Retrieve recent transactions for an account
    List<Transaction> findByUserIdAndTimestampAfter(UUID userId, LocalDateTime since);

    // Card-testing defense: Find all attempts across the network for a specific card token
    List<Transaction> findByCardTokenAndTimestampAfter(String cardToken, LocalDateTime since);

    // Bot/Scripting defense: Count high-frequency attempts from a single session
    long countBySessionIdAndTimestampAfter(UUID sessionId, LocalDateTime since);
}
