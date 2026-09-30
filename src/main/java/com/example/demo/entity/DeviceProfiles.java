package com.example.demo.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(
    name = "device_profiles",
    indexes = {
        @Index(name = "idx_device_fingerprint", columnList = "device_fingerprint"),
        @Index(name = "idx_profile_status", columnList = "profile_status")
    }
)
@Schema(name = "DeviceProfiles", description = "Model class for maintaining risk state per physical device")
public class DeviceProfiles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long profileId;

    @Column(name = "device_fingerprint", unique = true)
    private String deviceFingerprint;

    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull(message = "Created timestamp cannot be null")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @NotNull(message = "Updated timestamp cannot be null")
    private LocalDateTime updatedAt;

    @Column(name = "behavioral_risk_score", columnDefinition = "DECIMAL(5,2) DEFAULT 0.00")
    private BigDecimal behavioralRiskScore = BigDecimal.ZERO;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "os_version")
    private String osVersion;

    @Column(name = "app_version")
    private String appVersion;

    @Column(name = "profile_status", nullable = false)
    @NotEmpty(message = "Profile status cannot be empty")
    private String profileStatus;

    // Non-owning side of Many-To-Many: mappedBy refers to 'deviceProfiles' field in Accounts
    @JsonIgnore
    @ManyToMany(mappedBy = "deviceProfiles")
    private List<Accounts> accounts = new ArrayList<>();

    public DeviceProfiles() {
    }

    public DeviceProfiles(Long profileId, String deviceFingerprint, LocalDateTime createdAt,
                          LocalDateTime updatedAt, BigDecimal behavioralRiskScore, String ipAddress,
                          String osVersion, String appVersion, String profileStatus) {
        this.profileId = profileId;
        this.deviceFingerprint = deviceFingerprint;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.behavioralRiskScore = behavioralRiskScore;
        this.ipAddress = ipAddress;
        this.osVersion = osVersion;
        this.appVersion = appVersion;
        this.profileStatus = profileStatus;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public String getDeviceFingerprint() {
        return deviceFingerprint;
    }

    public void setDeviceFingerprint(String deviceFingerprint) {
        this.deviceFingerprint = deviceFingerprint;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public BigDecimal getBehavioralRiskScore() {
        return behavioralRiskScore;
    }

    public void setBehavioralRiskScore(BigDecimal behavioralRiskScore) {
        this.behavioralRiskScore = behavioralRiskScore;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getProfileStatus() {
        return profileStatus;
    }

    public void setProfileStatus(String profileStatus) {
        this.profileStatus = profileStatus;
    }

    public List<Accounts> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Accounts> accounts) {
        this.accounts = accounts;
    }
}