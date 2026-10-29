package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Decision;
import com.example.demo.entity.Transaction;
import com.example.demo.repository.DecisionRepository;
import com.example.demo.repository.TransactionRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/decisions")
@Tag(name = "Decisions Controller", description = "APIs for managing rule decisions and risk evaluations")
public class DecisionController {

    // 1. Initialize the Logger instance
    private static final Logger logger = LoggerFactory.getLogger(DecisionController.class);

    private final DecisionRepository decisionRepository;
    private final TransactionRepository transactionRepository;

    public DecisionController(DecisionRepository decisionRepository,
                              TransactionRepository transactionRepository) {
        this.decisionRepository = decisionRepository;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping
    @Operation(summary = "Get all decisions", description = "Retrieves a list of all recorded decisions")
    public List<Decision> getAllDecisions() {
        logger.debug("Fetching all decision evaluation records from database");
        return decisionRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get decision by ID", description = "Retrieves specific decision details by UUID")
    public ResponseEntity<Decision> getDecisionById(@PathVariable("id") UUID id) {
        logger.debug("Fetching decision by ID: {}", id);
        return decisionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("Decision not found with ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    @Operation(summary = "Create a new decision", description = "Records a new decision evaluation in the database")
    public ResponseEntity<Decision> createDecision(@Valid @RequestBody Decision decision) {
        logger.info("Recording new risk evaluation decision");

        UUID transactionId = decision.getTransaction() == null
                ? null
                : decision.getTransaction().getTransactionId();
                
        if (transactionId == null) {
            logger.warn("Validation failed: Transaction ID is missing in the decision request payload");
            return ResponseEntity.badRequest().build();
        }

        Transaction transaction = transactionRepository.findById(transactionId).orElse(null);
        if (transaction == null) {
            logger.warn("Validation failed: Referenced transaction not found with ID: {}", transactionId);
            return ResponseEntity.badRequest().build();
        }

        decision.setAssessmentId(null);
        decision.setTransaction(transaction);
        
        Decision savedDecision = decisionRepository.save(decision);
        logger.info("Successfully recorded decision with ID: {} | Action Taken: {} | Latency: {}ms", 
                savedDecision.getAssessmentId(), savedDecision.getActionTaken(), savedDecision.getLatencyMs());
        
        return new ResponseEntity<>(savedDecision, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update full decision details", description = "Replaces all existing attributes of a decision entity")
    public ResponseEntity<Decision> updateDecision(@PathVariable("id") UUID id,
                                                   @Valid @RequestBody Decision updatedDecision) {
        logger.info("Updating full decision details for ID: {}", id);
        return decisionRepository.findById(id)
                .map(existing -> {
                    existing.setRiskScore(updatedDecision.getRiskScore());
                    existing.setActionTaken(updatedDecision.getActionTaken());
                    existing.setRuleFlags(updatedDecision.getRuleFlags());
                    existing.setShapReasonCodes(updatedDecision.getShapReasonCodes());
                    existing.setLatencyMs(updatedDecision.getLatencyMs());
                    existing.setGroundTruthLabel(updatedDecision.getGroundTruthLabel());

                    Decision saved = decisionRepository.save(existing);
                    logger.info("Successfully updated decision ID: {}", id);
                    return ResponseEntity.ok(saved);
                })
                .orElseGet(() -> {
                    logger.warn("Attempted to update non-existent decision ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update decision", description = "Updates risk score, action, rule flags, reason codes, latency, or ground-truth label")
    public ResponseEntity<Decision> patchDecision(@PathVariable("id") UUID id,
                                                  @RequestBody Map<String, Object> updates) {
        logger.info("Applying partial updates (PATCH) to decision ID: {}", id);
        return decisionRepository.findById(id)
                .map(existing -> {
                    updates.forEach((key, value) -> {
                        switch (key) {
                            case "riskScore":
                                if (value instanceof Number number) {
                                    existing.setRiskScore(number.intValue());
                                }
                                break;
                            case "actionTaken":
                                existing.setActionTaken((String) value);
                                break;
                            case "ruleFlags":
                                if (value instanceof Map<?, ?> map) {
                                    existing.setRuleFlags(toStringObjectMap(map));
                                }
                                break;
                            case "shapReasonCodes":
                                if (value instanceof Map<?, ?> map) {
                                    existing.setShapReasonCodes(toStringObjectMap(map));
                                }
                                break;
                            case "latencyMs":
                                if (value instanceof Number number) {
                                    existing.setLatencyMs(number.intValue());
                                }
                                break;
                            case "groundTruthLabel":
                                if (value instanceof Number number) {
                                    existing.setGroundTruthLabel(number.intValue());
                                }
                                break;
                        }
                    });

                    Decision saved = decisionRepository.save(existing);
                    logger.info("Successfully patched decision ID: {}", id);
                    return ResponseEntity.ok(saved);
                })
                .orElseGet(() -> {
                    logger.warn("Attempted to patch non-existent decision ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete decision by ID", description = "Deletes a decision record permanently by UUID")
    public ResponseEntity<Void> deleteDecision(@PathVariable("id") UUID id) {
        logger.info("Deleting decision record with ID: {}", id);
        if (decisionRepository.existsById(id)) {
            decisionRepository.deleteById(id);
            logger.info("Successfully deleted decision ID: {}", id);
            return ResponseEntity.noContent().build();
        }
        logger.warn("Attempted to delete non-existent decision ID: {}", id);
        return ResponseEntity.notFound().build();
    }

    private Map<String, Object> toStringObjectMap(Map<?, ?> source) {
        Map<String, Object> result = new java.util.HashMap<>();
        source.forEach((key, value) -> {
            if (key instanceof String stringKey) {
                result.put(stringKey, value);
            }
        });
        return result;
    }
}