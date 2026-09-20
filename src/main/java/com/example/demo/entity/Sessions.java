package com.example.demo.entity;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
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
@Schema(name = "Sessions", description = "This is model class of session, it contains property and getter-setter methods")
public class Sessions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long session_id;

    @Column(name = "user_id")
    private Long user_id;

    @Column(name = "device_fingerprint")
    private String device_fingerprint;

    @Column(name = "ip_address")
    private String ip_address;

    @Column(name = "is_vpn_proxy", columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean is_vpn_proxy = false;

    @Column(name = "asn_datacenter_flag", columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean asn_datacenter_flag = false;

    @Column(name = "geo_country", nullable = false)
    @NotEmpty(message = "Geo country cannot be empty")
    private String geo_country;

    @Column(name = "user_agent", nullable = false)
    @NotEmpty(message = "User agent cannot be empty")
    private String user_agent;

    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull(message = "Created timestamp cannot be null")
    private LocalDateTime created_at;

    public Sessions() {

    }

    public Sessions(Long session_id, Long user_id, String device_fingerprint, String ip_address, 
                    Boolean is_vpn_proxy, Boolean asn_datacenter_flag, 
                    @NotEmpty String geo_country, @NotEmpty String user_agent, 
                    @NotNull LocalDateTime created_at) {
        this.session_id = session_id;
        this.user_id = user_id;
        this.device_fingerprint = device_fingerprint;
        this.ip_address = ip_address;
        this.is_vpn_proxy = is_vpn_proxy;
        this.asn_datacenter_flag = asn_datacenter_flag;
        this.geo_country = geo_country;
        this.user_agent = user_agent;
        this.created_at = created_at;
    }

    public Long getSessionId() {
        return session_id;
    }

    public void setSessionId(Long session_id) {
        this.session_id = session_id;
    }

    public Long getUserId() {
        return user_id;
    }

    public void setUserId(Long user_id) {
        this.user_id = user_id;
    }

    public String getDeviceFingerprint() {
        return device_fingerprint;
    }

    public void setDeviceFingerprint(String device_fingerprint) {
        this.device_fingerprint = device_fingerprint;
    }

    public String getIpAddress() {
        return ip_address;
    }

    public void setIpAddress(String ip_address) {
        this.ip_address = ip_address;
    }

    public Boolean getIsVpnProxy() {
        return is_vpn_proxy;
    }

    public void setIsVpnProxy(Boolean is_vpn_proxy) {
        this.is_vpn_proxy = is_vpn_proxy;
    }

    public Boolean getAsnDatacenterFlag() {
        return asn_datacenter_flag;
    }

    public void setAsnDatacenterFlag(Boolean asn_datacenter_flag) {
        this.asn_datacenter_flag = asn_datacenter_flag;
    }

    public String getGeoCountry() {
        return geo_country;
    }

    public void setGeoCountry(String geo_country) {
        this.geo_country = geo_country;
    }

    public String getUserAgent() {
        return user_agent;
    }

    public void setUserAgent(String user_agent) {
        this.user_agent = user_agent;
    }

    public LocalDateTime getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "Sessions [session_id=" + session_id + ", user_id=" + user_id + ", device_fingerprint=" 
                + device_fingerprint + ", ip_address=" + ip_address + ", is_vpn_proxy=" + is_vpn_proxy 
                + ", asn_datacenter_flag=" + asn_datacenter_flag + ", geo_country=" + geo_country 
                + ", user_agent=" + user_agent + ", created_at=" + created_at + "]";
    }
}