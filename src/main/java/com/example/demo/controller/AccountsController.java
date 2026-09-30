package com.example.demo.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.example.demo.repository.AccountsRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Accounts Controller", description = "APIs for managing user accounts")
public class AccountsController {

    @Autowired
    private AccountsRepository accountsRepository;

    @GetMapping
    @Operation(summary = "Get all accounts")
    public ResponseEntity<List<Accounts>> getAllAccounts() {
        List<Accounts> accounts = accountsRepository.findAll();
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get account by User ID")
    public ResponseEntity<Accounts> getAccountById(@PathVariable("id") UUID userId) {
        return accountsRepository.findById(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/kyc-status")
    @Operation(summary = "Get accounts by KYC status")
    public ResponseEntity<List<Accounts>> getAccountsByKycStatus(@RequestParam String status) {
        List<Accounts> accounts = accountsRepository.findByKycStatus(status);
        return ResponseEntity.ok(accounts);
    }

    @PostMapping
    @Operation(summary = "Create a new account")
    public ResponseEntity<Accounts> createAccount(@Valid @RequestBody Accounts account) {
        account.setUserId(null);
        Accounts savedAccount = accountsRepository.save(account);
        return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing account")
    public ResponseEntity<Accounts> updateAccount(@PathVariable("id") UUID userId, 
                                                   @Valid @RequestBody Accounts accountDetails) {
        return accountsRepository.findById(userId)
                .map(existingAccount -> {
                    existingAccount.setHomeZip(accountDetails.getHomeZip());
                    existingAccount.setKycStatus(accountDetails.getKycStatus());
                    existingAccount.setHistoricalAvgSpend(accountDetails.getHistoricalAvgSpend());
                    existingAccount.setHistoricalTxCount(accountDetails.getHistoricalTxCount());
                    existingAccount.setIsSuspended(accountDetails.getIsSuspended());
                    Accounts updatedAccount = accountsRepository.save(existingAccount);
                    return ResponseEntity.ok(updatedAccount);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update an account", description = "Updates specific fields of an account (e.g., kycStatus, isSuspended, or historical metrics)")
    public ResponseEntity<Accounts> patchAccount(@PathVariable("id") UUID userId,
                                                 @RequestBody Map<String, Object> updates) {
        return accountsRepository.findById(userId)
                .map(existingAccount -> {
                    updates.forEach((key, value) -> {
                        switch (key) {
                            case "homeZip":
                                existingAccount.setHomeZip((String) value);
                                break;
                            case "kycStatus":
                                existingAccount.setKycStatus((String) value);
                                break;
                            case "historicalAvgSpend":
                                if (value instanceof Number) {
                                    existingAccount.setHistoricalAvgSpend(new BigDecimal(value.toString()));
                                }
                                break;
                            case "historicalTxCount":
                                if (value instanceof Number) {
                                    existingAccount.setHistoricalTxCount(((Number) value).intValue());
                                }
                                break;
                            case "isSuspended":
                                if (value instanceof Boolean) {
                                    existingAccount.setIsSuspended((Boolean) value);
                                }
                                break;
                        }
                    });
                    Accounts patchedAccount = accountsRepository.save(existingAccount);
                    return ResponseEntity.ok(patchedAccount);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an account by User ID")
    public ResponseEntity<Void> deleteAccount(@PathVariable("id") UUID userId) {
        return accountsRepository.findById(userId)
                .map(account -> {
                    accountsRepository.delete(account);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}