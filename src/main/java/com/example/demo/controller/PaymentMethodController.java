package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.PaymentMethods;
import com.example.demo.repository.PaymentMethodRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/payment-methods")
@Tag(name = "Payment Methods", description = "APIs for managing tokenized payment methods")
public class PaymentMethodController {

    private final PaymentMethodRepository repository;

    public PaymentMethodController(
            PaymentMethodRepository repository) {
        this.repository = repository;
    }

    // CREATE
    @PostMapping
    @Operation(summary = "Create payment method", description = "Stores a tokenized payment method")
    public ResponseEntity<PaymentMethods> createPaymentMethod(
            @Valid @RequestBody PaymentMethods paymentMethod) {

        if (paymentMethod.getCreatedAt() == null) {
            paymentMethod.setCreatedAt(LocalDateTime.now());
        }

        PaymentMethods savedPaymentMethod = repository.save(paymentMethod);

        return new ResponseEntity<>(
                savedPaymentMethod,
                HttpStatus.CREATED);
    }

    // GET ALL
    @GetMapping
    @Operation(summary = "Get all payment methods", description = "Returns all stored payment methods")
    public ResponseEntity<List<PaymentMethods>> getAllPaymentMethods() {

        return ResponseEntity.ok(
                repository.findAll());
    }

    // GET BY CARD TOKEN
    @GetMapping("/{cardToken}")
    @Operation(summary = "Get payment method by card token", description = "Finds a payment method using its token")
    public ResponseEntity<PaymentMethods> getPaymentMethod(
            @PathVariable String cardToken) {

        return repository.findById(cardToken)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET BY USER
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get payment methods by user", description = "Returns payment methods belonging to a user")
    public ResponseEntity<List<PaymentMethods>> getByUser(
            @PathVariable UUID userId) {

        return ResponseEntity.ok(
                repository.findByUserId(userId));
    }

    // GET BY BIN
    @GetMapping("/bin/{binNumber}")
    @Operation(summary = "Get payment methods by BIN", description = "Returns payment methods associated with a BIN")
    public ResponseEntity<List<PaymentMethods>> getByBin(
            @PathVariable String binNumber) {

        return ResponseEntity.ok(
                repository.findByBinNumber(binNumber));
    }

    // UPDATE
    @PutMapping("/{cardToken}")
    @Operation(summary = "Update payment method", description = "Updates an existing payment method")
    public ResponseEntity<PaymentMethods> updatePaymentMethod(
            @PathVariable String cardToken,
            @Valid @RequestBody PaymentMethods paymentMethod) {

        return repository.findById(cardToken)
                .map(existing -> {

                    existing.setUserId(paymentMethod.getUserId());
                    existing.setBinNumber(paymentMethod.getBinNumber());
                    existing.setCardType(paymentMethod.getCardType());
                    existing.setIssuerBank(paymentMethod.getIssuerBank());
                    existing.setCardCountry(paymentMethod.getCardCountry());
                    existing.setCardStatus(paymentMethod.getCardStatus());

                    PaymentMethods updated = repository.save(existing);

                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{cardToken}")
    @Operation(summary = "Delete payment method", description = "Deletes a payment method using its token")
    public ResponseEntity<Void> deletePaymentMethod(
            @PathVariable String cardToken) {

        if (!repository.existsById(cardToken)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(cardToken);

        return ResponseEntity.noContent().build();
    }
}