package com.example.demo.entity;
import java.util.UUID; // <--- Add this import statement

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(
    name = "accounts",
    indexes = {
        @Index(name = "idx_home_zip", columnList = "home_zip"),
        @Index(name = "idx_kyc_status", columnList = "kyc_status")
    }
)
@Schema(name = "Accounts", description = "Model class for user accounts")
public class Accounts {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "account_created_at", nullable = false, updatable = false)
    @NotNull(message = "Account creation timestamp cannot be null")
    private LocalDateTime accountCreatedAt;

    @Column(name = "home_zip", nullable = false)
    @NotEmpty(message = "Home zip cannot be empty")
    private String homeZip;

    @Column(name = "kyc_status", nullable = false)
    @NotEmpty(message = "KYC status cannot be empty")
    private String kycStatus;

    @Column(name = "historical_avg_spend", columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
    private BigDecimal historicalAvgSpend = BigDecimal.ZERO;

    @Column(name = "historical_tx_count", columnDefinition = "INTEGER DEFAULT 0")
    private Integer historicalTxCount = 0;

    @Column(name = "is_suspended", columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isSuspended = false;

    // Non-owning side: One Account has Many Sessions (mappedBy refers to 'account' field in Sessions)
    @JsonIgnore
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Sessions> sessions = new ArrayList<>();

    // Non-owning side: One Account has Many Payment Methods (mappedBy refers to 'account' field in PaymentMethods)
    @JsonIgnore
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<PaymentMethods> paymentMethods = new ArrayList<>();

    // Owning side of Many-To-Many: Uses @JoinTable for account_device junction
    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "account_device",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "profile_id")
    )
    private List<DeviceProfiles> deviceProfiles = new ArrayList<>();

    public Accounts() {
    }

    public Accounts(UUID userId, LocalDateTime accountCreatedAt, String homeZip,
                    String kycStatus, BigDecimal historicalAvgSpend,
                    Integer historicalTxCount, Boolean isSuspended) {
        this.userId = userId;
        this.accountCreatedAt = accountCreatedAt;
        this.homeZip = homeZip;
        this.kycStatus = kycStatus;
        this.historicalAvgSpend = historicalAvgSpend;
        this.historicalTxCount = historicalTxCount;
        this.isSuspended = isSuspended;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public LocalDateTime getAccountCreatedAt() {
        return accountCreatedAt;
    }

    public void setAccountCreatedAt(LocalDateTime accountCreatedAt) {
        this.accountCreatedAt = accountCreatedAt;
    }

    public String getHomeZip() {
        return homeZip;
    }

    public void setHomeZip(String homeZip) {
        this.homeZip = homeZip;
    }

    public String getKycStatus() {
        return kycStatus;
    }

    public void setKycStatus(String kycStatus) {
        this.kycStatus = kycStatus;
    }

    public BigDecimal getHistoricalAvgSpend() {
        return historicalAvgSpend;
    }

    public void setHistoricalAvgSpend(BigDecimal historicalAvgSpend) {
        this.historicalAvgSpend = historicalAvgSpend;
    }

    public Integer getHistoricalTxCount() {
        return historicalTxCount;
    }

    public void setHistoricalTxCount(Integer historicalTxCount) {
        this.historicalTxCount = historicalTxCount;
    }

    public Boolean getIsSuspended() {
        return isSuspended;
    }

    public void setIsSuspended(Boolean isSuspended) {
        this.isSuspended = isSuspended;
    }

    public List<Sessions> getSessions() {
        return sessions;
    }

    public void setSessions(List<Sessions> sessions) {
        this.sessions = sessions;
    }

    public List<PaymentMethods> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(List<PaymentMethods> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public List<DeviceProfiles> getDeviceProfiles() {
        return deviceProfiles;
    }

    public void setDeviceProfiles(List<DeviceProfiles> deviceProfiles) {
        this.deviceProfiles = deviceProfiles;
    }
}