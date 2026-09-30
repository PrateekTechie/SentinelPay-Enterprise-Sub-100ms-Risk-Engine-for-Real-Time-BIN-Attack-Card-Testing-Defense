package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Accounts;
import com.example.demo.entity.PaymentMethods;
import com.example.demo.repository.AccountsRepository;
import com.example.demo.repository.PaymentMethodRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/payment-methods")
@Tag(name = "Payment Methods Controller", description = "APIs for managing payment methods")
public class PaymentMethodController {

    private final PaymentMethodRepository paymentMethodRepository;
    private final AccountsRepository accountsRepository;

    public PaymentMethodController(PaymentMethodRepository paymentMethodRepository, 
                                   AccountsRepository accountsRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
        this.accountsRepository = accountsRepository;
    }

    @GetMapping
    @Operation(summary = "Get all payment methods")
    public List<PaymentMethods> getAllPaymentMethods() {
        return paymentMethodRepository.findAll();
    }

    @GetMapping("/{cardToken}")
    @Operation(summary = "Get payment method by card token")
    public ResponseEntity<PaymentMethods> getPaymentMethodByToken(@PathVariable("cardToken") String cardToken) {
        return paymentMethodRepository.findById(cardToken)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new payment method")
    public ResponseEntity<?> createPaymentMethod(@Valid @RequestBody PaymentMethods paymentMethod, 
                                                 @RequestParam("userId") UUID userId) {
        return accountsRepository.findById(userId)
                .map(account -> {
                    paymentMethod.setAccount(account);
                    PaymentMethods saved = paymentMethodRepository.save(paymentMethod);
                    return new ResponseEntity<>(saved, HttpStatus.CREATED);
                })
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/{cardToken}")
    @Operation(summary = "Update an existing payment method")
    public ResponseEntity<PaymentMethods> updatePaymentMethod(@PathVariable("cardToken") String cardToken,
                                                               @Valid @RequestBody PaymentMethods updatedData,
                                                               @RequestParam(value = "userId", required = false) UUID userId) {
        return paymentMethodRepository.findById(cardToken)
                .map(existing -> {
                    existing.setBinNumber(updatedData.getBinNumber());
                    existing.setCardType(updatedData.getCardType());
                    existing.setIssuerBank(updatedData.getIssuerBank());
                    existing.setCardCountry(updatedData.getCardCountry());
                    existing.setCardStatus(updatedData.getCardStatus());

                    if (userId != null) {
                        Accounts account = accountsRepository.findById(userId).orElse(null);
                        existing.setAccount(account);
                    }

                    PaymentMethods saved = paymentMethodRepository.save(existing);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{cardToken}")
    @Operation(summary = "Partially update a payment method", description = "Updates specific fields such as cardStatus, issuerBank, or re-associates userId")
    public ResponseEntity<PaymentMethods> patchPaymentMethod(@PathVariable("cardToken") String cardToken,
                                                             @RequestBody Map<String, Object> updates,
                                                             @RequestParam(value = "userId", required = false) UUID userId) {
        return paymentMethodRepository.findById(cardToken)
                .map(existing -> {
                    updates.forEach((key, value) -> {
                        switch (key) {
                            case "binNumber":
                                existing.setBinNumber((String) value);
                                break;
                            case "cardType":
                                existing.setCardType((String) value);
                                break;
                            case "issuerBank":
                                existing.setIssuerBank((String) value);
                                break;
                            case "cardCountry":
                                existing.setCardCountry((String) value);
                                break;
                            case "cardStatus":
                                existing.setCardStatus((String) value);
                                break;
                        }
                    });

                    if (userId != null) {
                        accountsRepository.findById(userId).ifPresent(existing::setAccount);
                    }

                    PaymentMethods saved = paymentMethodRepository.save(existing);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{cardToken}")
    @Operation(summary = "Delete a payment method by card token")
    public ResponseEntity<Void> deletePaymentMethod(@PathVariable("cardToken") String cardToken) {
        if (paymentMethodRepository.existsById(cardToken)) {
            paymentMethodRepository.deleteById(cardToken);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}