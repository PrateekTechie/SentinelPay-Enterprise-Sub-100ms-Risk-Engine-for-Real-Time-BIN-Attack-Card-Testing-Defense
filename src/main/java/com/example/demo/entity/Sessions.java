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
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(
    name = "sessions",
    indexes = {
        @Index(name = "idx_device_fingerprint", columnList = "device_fingerprint"),
        @Index(name = "idx_ip_address", columnList = "ip_address")
    }
)
@Schema(name = "Sessions", description = "Model class for user authentication sessions")
public class Sessions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long sessionId;

    // Owning side: Contains FK 'user_id' pointing to Accounts
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Accounts account;

    @Column(name = "device_fingerprint")
    private String deviceFingerprint;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "is_vpn_proxy", columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isVpnProxy = false;

    @Column(name = "asn_datacenter_flag", columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean asnDatacenterFlag = false;

    @Column(name = "geo_country", nullable = false)
    @NotEmpty(message = "Geo country cannot be empty")
    private String geoCountry;

    @Column(name = "user_agent", nullable = false)
    @NotEmpty(message = "User agent cannot be empty")
    private String userAgent;

    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull(message = "Created timestamp cannot be null")
    private LocalDateTime createdAt;

    // Non-owning side: mappedBy refers to 'session' field in Transaction
    @JsonIgnore
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();

    public Sessions() {
    }

    public Sessions(Long sessionId, Accounts account, String deviceFingerprint, String ipAddress,
                    Boolean isVpnProxy, Boolean asnDatacenterFlag, String geoCountry,
                    String userAgent, LocalDateTime createdAt) {
        this.sessionId = sessionId;
        this.account = account;
        this.deviceFingerprint = deviceFingerprint;
        this.ipAddress = ipAddress;
        this.isVpnProxy = isVpnProxy;
        this.asnDatacenterFlag = asnDatacenterFlag;
        this.geoCountry = geoCountry;
        this.userAgent = userAgent;
        this.createdAt = createdAt;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Accounts getAccount() {
        return account;
    }

    public void setAccount(Accounts account) {
        this.account = account;
    }

    public String getDeviceFingerprint() {
        return deviceFingerprint;
    }

    public void setDeviceFingerprint(String deviceFingerprint) {
        this.deviceFingerprint = deviceFingerprint;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Boolean getIsVpnProxy() {
        return isVpnProxy;
    }

    public void setIsVpnProxy(Boolean isVpnProxy) {
        this.isVpnProxy = isVpnProxy;
    }

    public Boolean getAsnDatacenterFlag() {
        return asnDatacenterFlag;
    }

    public void setAsnDatacenterFlag(Boolean asnDatacenterFlag) {
        this.asnDatacenterFlag = asnDatacenterFlag;
    }

    public String getGeoCountry() {
        return geoCountry;
    }

    public void setGeoCountry(String geoCountry) {
        this.geoCountry = geoCountry;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
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