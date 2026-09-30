package com.example.demo.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(
    name = "transactions",
    indexes = {
        @Index(name = "idx_transactions_timestamp", columnList = "timestamp"),
        @Index(name = "idx_transactions_card_token", columnList = "card_token"),
        @Index(name = "idx_transactions_session_id", columnList = "session_id")
    }
)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "transaction_id", updatable = false, nullable = false)
    private UUID transactionId;

    // Owning side: Foreign Key 'card_token'
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_token", referencedColumnName = "card_token")
    private PaymentMethods paymentMethod;

    // Owning side: Foreign Key 'session_id'
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", referencedColumnName = "session_id")
    private Sessions session;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
    @Column(name = "amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(name = "currency", length = 3)
    private String currency;

    @NotNull(message = "Merchant ID is required")
    @Column(name = "merchant_id", nullable = false)
    private String merchantId;

    @NotNull(message = "MCC Code is required")
    @Column(name = "mcc_code", nullable = false, length = 4)
    private String mccCode;

    @NotNull(message = "Timestamp is required")
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    // Non-owning side of 1:1: Decision table holds the transaction_id FK
    @JsonIgnore
    @OneToOne(mappedBy = "transaction", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Decision decision;

    public Transaction() {
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public PaymentMethods getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethods paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Sessions getSession() {
        return session;
    }

    public void setSession(Sessions session) {
        this.session = session;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMccCode() {
        return mccCode;
    }

    public void setMccCode(String mccCode) {
        this.mccCode = mccCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Decision getDecision() {
        return decision;
    }

    public void setDecision(Decision decision) {
        this.decision = decision;
    }
}