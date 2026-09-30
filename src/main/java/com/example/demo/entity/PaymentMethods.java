package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "payment_methods")
@Schema(name = "PaymentMethods", description = "Payment method stored using a tokenized card identifier")
public class PaymentMethods {

    @Id
    @Column(name = "card_token", nullable = false, unique = true)
    @NotEmpty(message = "Card token cannot be empty")
    private String cardToken;

    // Owning side: Contains FK 'user_id' pointing to Accounts
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Accounts account;

    @Column(name = "bin_number", nullable = false)
    @NotEmpty(message = "BIN number cannot be empty")
    private String binNumber;

    @Column(name = "card_type", nullable = false)
    @NotEmpty(message = "Card type cannot be empty")
    private String cardType;

    @Column(name = "issuer_bank", nullable = false)
    @NotEmpty(message = "Issuer bank cannot be empty")
    private String issuerBank;

    @Column(name = "card_country", nullable = false)
    @NotEmpty(message = "Card country cannot be empty")
    private String cardCountry;

    @Column(name = "card_status", nullable = false)
    @NotEmpty(message = "Card status cannot be empty")
    private String cardStatus;

    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull(message = "Created timestamp cannot be null")
    private LocalDateTime createdAt = LocalDateTime.now();

    // Non-owning side: mappedBy refers to 'paymentMethod' field in Transaction
    @JsonIgnore
    @OneToMany(mappedBy = "paymentMethod", cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();

    public PaymentMethods() {
        this.createdAt = LocalDateTime.now();
    }

    public PaymentMethods(String cardToken, Accounts account, String binNumber, String cardType,
                          String issuerBank, String cardCountry, String cardStatus, LocalDateTime createdAt) {
        this.cardToken = cardToken;
        this.account = account;
        this.binNumber = binNumber;
        this.cardType = cardType;
        this.issuerBank = issuerBank;
        this.cardCountry = cardCountry;
        this.cardStatus = cardStatus;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }

    public String getCardToken() {
        return cardToken;
    }

    public void setCardToken(String cardToken) {
        this.cardToken = cardToken;
    }

    public Accounts getAccount() {
        return account;
    }

    public void setAccount(Accounts account) {
        this.account = account;
    }

    public String getBinNumber() {
        return binNumber;
    }

    public void setBinNumber(String binNumber) {
        this.binNumber = binNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getIssuerBank() {
        return issuerBank;
    }

    public void setIssuerBank(String issuerBank) {
        this.issuerBank = issuerBank;
    }

    public String getCardCountry() {
        return cardCountry;
    }

    public void setCardCountry(String cardCountry) {
        this.cardCountry = cardCountry;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}