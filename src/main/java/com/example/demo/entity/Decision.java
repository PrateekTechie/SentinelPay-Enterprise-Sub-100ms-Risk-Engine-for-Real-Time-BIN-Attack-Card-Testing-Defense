package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "decisions")
public class Decision {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "assessment_id", nullable = false, updatable = false)
    private UUID assessmentId;

    // Owning side: Contains FK 'transaction_id' pointing to Transaction
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", referencedColumnName = "transaction_id", nullable = false, unique = true)
    private Transaction transaction;

    @Min(1)
    @Max(100)
    @Column(name = "risk_score", nullable = false)
    private Integer riskScore;

    @Column(name = "action_taken", nullable = false)
    private String actionTaken;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "rule_flags", columnDefinition = "jsonb")
    private Map<String, Object> ruleFlags;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "shap_reason_codes", columnDefinition = "jsonb")
    private Map<String, Object> shapReasonCodes;

    @Column(name = "latency_ms", nullable = false)
    private Integer latencyMs;

    @Min(0)
    @Max(1)
    @Column(name = "ground_truth_label")
    private Integer groundTruthLabel;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Decision() {
    }

    public UUID getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(UUID assessmentId) {
        this.assessmentId = assessmentId;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Integer getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(Integer riskScore) {
        this.riskScore = riskScore;
    }

    public String getActionTaken() {
        return actionTaken;
    }

    public void setActionTaken(String actionTaken) {
        this.actionTaken = actionTaken;
    }

    public Map<String, Object> getRuleFlags() {
        return ruleFlags;
    }

    public void setRuleFlags(Map<String, Object> ruleFlags) {
        this.ruleFlags = ruleFlags;
    }

    public Map<String, Object> getShapReasonCodes() {
        return shapReasonCodes;
    }

    public void setShapReasonCodes(Map<String, Object> shapReasonCodes) {
        this.shapReasonCodes = shapReasonCodes;
    }

    public Integer getLatencyMs() {
        return latencyMs;
    }

    public void setLatencyMs(Integer latencyMs) {
        this.latencyMs = latencyMs;
    }

    public Integer getGroundTruthLabel() {
        return groundTruthLabel;
    }

    public void setGroundTruthLabel(Integer groundTruthLabel) {
        this.groundTruthLabel = groundTruthLabel;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}