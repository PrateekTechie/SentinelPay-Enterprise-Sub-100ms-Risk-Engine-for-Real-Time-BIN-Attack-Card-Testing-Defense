package com.example.demo.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Transaction;
import com.example.demo.entity.PaymentMethods;
import com.example.demo.entity.Sessions;
import com.example.demo.repository.PaymentMethodRepository;
import com.example.demo.repository.SessionsRepository;
import com.example.demo.repository.TransactionRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@RestController
@RequestMapping("/api/v1/transactions")
@Tag(name = "Transactions Controller", description = "APIs for managing and evaluating financial transactions")
public class TransactionController {

    private final TransactionRepository transactionRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final SessionsRepository sessionsRepository;

    public TransactionController(TransactionRepository transactionRepository,
                                 PaymentMethodRepository paymentMethodRepository,
                                 SessionsRepository sessionsRepository) {
        this.transactionRepository = transactionRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.sessionsRepository = sessionsRepository;
    }

    @GetMapping
    @Operation(summary = "Get all transactions", description = "Retrieves a list of all recorded transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionRepository.findAll());
    }

    @PostMapping
    @Operation(summary = "Ingest a new transaction", description = "Saves a new transaction payload into the risk engine database")
    public ResponseEntity<?> ingestTransaction(@Valid @RequestBody TransactionRequest request) {
        PaymentMethods paymentMethod = paymentMethodRepository.findById(request.cardToken()).orElse(null);
        if (paymentMethod == null) {
            return ResponseEntity.badRequest().body("No payment method found for cardToken: " + request.cardToken());
        }

        Sessions session = null;
        if (request.sessionId() != null) {
            session = sessionsRepository.findById(request.sessionId()).orElse(null);
            if (session == null) {
                return ResponseEntity.badRequest().body("No session found for sessionId: " + request.sessionId());
            }
        }

        Transaction transaction = new Transaction();
        transaction.setPaymentMethod(paymentMethod);
        transaction.setSession(session);
        transaction.setAmount(request.amount());
        transaction.setCurrency(request.currency());
        transaction.setMerchantId(request.merchantId());
        transaction.setMccCode(request.mccCode());
        transaction.setTimestamp(request.timestamp() != null ? request.timestamp() : LocalDateTime.now());
        Transaction savedTransaction = transactionRepository.save(transaction);
        return new ResponseEntity<>(savedTransaction, HttpStatus.CREATED);
    }

    @GetMapping("/{transactionId}")
    @Operation(summary = "Get transaction by ID", description = "Retrieves specific transaction details by UUID")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable("transactionId") UUID transactionId) {
        return transactionRepository.findById(transactionId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{transactionId}")
    @Operation(summary = "Update full transaction details", description = "Replaces all existing attributes of a transaction")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable("transactionId") UUID transactionId,
                                                         @Valid @RequestBody Transaction updatedTransaction) {
        return transactionRepository.findById(transactionId)
                .map(existing -> {
                    existing.setAmount(updatedTransaction.getAmount());
                    existing.setCurrency(updatedTransaction.getCurrency());
                    existing.setMerchantId(updatedTransaction.getMerchantId());
                    existing.setMccCode(updatedTransaction.getMccCode());
                    existing.setTimestamp(updatedTransaction.getTimestamp());
                    
                    Transaction saved = transactionRepository.save(existing);
                    return ResponseEntity.ok(saved);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{transactionId}")
    @Operation(summary = "Partially update transaction", description = "Updates amount, currency, merchant, MCC, or timestamp")
    public ResponseEntity<Transaction> patchTransaction(@PathVariable("transactionId") UUID transactionId,
                                                        @RequestBody Map<String, Object> updates) {
        return transactionRepository.findById(transactionId)
                .map(existing -> {
                    updates.forEach((key, value) -> {
                        switch (key) {
                            case "amount":
                                if (value instanceof Number) {
                                    existing.setAmount(new BigDecimal(value.toString()));
                                }
                                break;
                            case "currency":
                                existing.setCurrency((String) value);
                                break;
                            case "merchantId":
                                existing.setMerchantId((String) value);
                                break;
                            case "mccCode":
                                existing.setMccCode((String) value);
                                break;
                            case "timestamp":
                                if (value != null) {
                                    existing.setTimestamp(LocalDateTime.parse(value.toString()));
                                }
                                break;
                        }
                    });

                    Transaction saved = transactionRepository.save(existing);
                    return ResponseEntity.ok(saved);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{transactionId}")
    @Operation(summary = "Delete transaction by ID", description = "Deletes a transaction record permanently by UUID")
    public ResponseEntity<Void> deleteTransaction(@PathVariable("transactionId") UUID transactionId) {
        if (transactionRepository.existsById(transactionId)) {
            transactionRepository.deleteById(transactionId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record TransactionRequest(
            @NotNull @Positive BigDecimal amount,
            String currency,
            @NotBlank String merchantId,
            @JsonAlias("merchantCategoryCode") @NotBlank @Size(max = 4) String mccCode,
            LocalDateTime timestamp,
            @NotBlank String cardToken,
            Long sessionId) {
    }
}