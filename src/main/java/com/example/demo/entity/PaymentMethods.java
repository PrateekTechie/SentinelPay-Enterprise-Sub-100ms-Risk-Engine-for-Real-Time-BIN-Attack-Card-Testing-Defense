package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "payment_methods")
@Schema(
        name = "PaymentMethods",
        description = "Payment method stored using a tokenized card identifier"
)
public class PaymentMethods {

    @Id
    @Column(name = "card_token", nullable = false, unique = true)
    @NotEmpty(message = "Card token cannot be empty")
    private String card_token;

    @Column(name = "user_id", nullable = false)
    @NotNull(message = "User ID cannot be null")
    private UUID user_id;

    @Column(name = "bin_number", nullable = false)
    @NotEmpty(message = "BIN number cannot be empty")
    private String bin_number;

    @Column(name = "card_type", nullable = false)
    @NotEmpty(message = "Card type cannot be empty")
    private String card_type;

    @Column(name = "issuer_bank", nullable = false)
    @NotEmpty(message = "Issuer bank cannot be empty")
    private String issuer_bank;

    @Column(name = "card_country", nullable = false)
    @NotEmpty(message = "Card country cannot be empty")
    private String card_country;

    @Column(name = "card_status", nullable = false)
    @NotEmpty(message = "Card status cannot be empty")
    private String card_status;

    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull(message = "Created timestamp cannot be null")
    private LocalDateTime created_at = LocalDateTime.now();

    // Default constructor
    public PaymentMethods() {
        this.created_at = LocalDateTime.now();
    }

    // Parameterized constructor
    public PaymentMethods(
            String card_token,
            UUID user_id,
            String bin_number,
            String card_type,
            String issuer_bank,
            String card_country,
            String card_status,
            LocalDateTime created_at) {

        this.card_token = card_token;
        this.user_id = user_id;
        this.bin_number = bin_number;
        this.card_type = card_type;
        this.issuer_bank = issuer_bank;
        this.card_country = card_country;
        this.card_status = card_status;
        this.created_at = created_at != null
                ? created_at
                : LocalDateTime.now();
    }

    public String getCardToken() {
        return card_token;
    }

    public void setCardToken(String card_token) {
        this.card_token = card_token;
    }

    public UUID getUserId() {
        return user_id;
    }

    public void setUserId(UUID user_id) {
        this.user_id = user_id;
    }

    public String getBinNumber() {
        return bin_number;
    }

    public void setBinNumber(String bin_number) {
        this.bin_number = bin_number;
    }

    public String getCardType() {
        return card_type;
    }

    public void setCardType(String card_type) {
        this.card_type = card_type;
    }

    public String getIssuerBank() {
        return issuer_bank;
    }

    public void setIssuerBank(String issuer_bank) {
        this.issuer_bank = issuer_bank;
    }

    public String getCardCountry() {
        return card_country;
    }

    public void setCardCountry(String card_country) {
        this.card_country = card_country;
    }

    public String getCardStatus() {
        return card_status;
    }

    public void setCardStatus(String card_status) {
        this.card_status = card_status;
    }

    public LocalDateTime getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "PaymentMethods [" +
                "card_token=" + card_token +
                ", user_id=" + user_id +
                ", bin_number=" + bin_number +
                ", card_type=" + card_type +
                ", issuer_bank=" + issuer_bank +
                ", card_country=" + card_country +
                ", card_status=" + card_status +
                ", created_at=" + created_at +
                "]";
    }
}