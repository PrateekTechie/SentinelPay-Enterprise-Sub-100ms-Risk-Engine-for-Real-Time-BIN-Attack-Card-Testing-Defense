package com.example.demo.controller;

import com.example.demo.entity.Sessions;
import com.example.demo.repository.SessionsRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sessions")
@Tag(name = "Sessions Controller", description = "Endpoints for managing session records")
public class SessionsController {

    @Autowired
    private SessionsRepository sessionsRepository;

    @GetMapping
    @Operation(summary = "Get all sessions", description = "Retrieves a list of all active or logged sessions")
    public ResponseEntity<List<Sessions>> getAllSessions() {
        return ResponseEntity.ok(sessionsRepository.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get session by ID", description = "Retrieves a specific session by its session_id")
    public ResponseEntity<Sessions> getSessionById(@PathVariable("id") Long id) {
        return sessionsRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new session", description = "Saves a new session entry into the database")
    public ResponseEntity<Sessions> createSession(@Valid @RequestBody Sessions session) {
        if (session.getCreatedAt() == null) {
            session.setCreatedAt(LocalDateTime.now());
        }
        Sessions savedSession = sessionsRepository.save(session);
        return new ResponseEntity<>(savedSession, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Replace/Update full session", description = "Replaces all details of an existing session record")
    public ResponseEntity<Sessions> updateSession(
            @PathVariable("id") Long id, 
            @Valid @RequestBody Sessions updatedSessionDetails) {

        return sessionsRepository.findById(id)
                .map(existingSession -> {
                    existingSession.setAccount(updatedSessionDetails.getAccount());
                    existingSession.setDeviceFingerprint(updatedSessionDetails.getDeviceFingerprint());
                    existingSession.setIpAddress(updatedSessionDetails.getIpAddress());
                    existingSession.setIsVpnProxy(updatedSessionDetails.getIsVpnProxy());
                    existingSession.setAsnDatacenterFlag(updatedSessionDetails.getAsnDatacenterFlag());
                    existingSession.setGeoCountry(updatedSessionDetails.getGeoCountry());
                    existingSession.setUserAgent(updatedSessionDetails.getUserAgent());

                    Sessions saved = sessionsRepository.save(existingSession);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update session fields", description = "Updates device, network, and location details of a session")
    public ResponseEntity<Sessions> patchSession(
            @PathVariable("id") Long id, 
            @RequestBody Map<String, Object> updates) {

        return sessionsRepository.findById(id)
                .map(existingSession -> {
                    updates.forEach((key, value) -> {
                        switch (key) {
                            case "deviceFingerprint":
                                existingSession.setDeviceFingerprint((String) value);
                                break;
                            case "ipAddress":
                                existingSession.setIpAddress((String) value);
                                break;
                            case "userAgent":
                                existingSession.setUserAgent((String) value);
                                break;
                            case "isVpnProxy":
                                if (value instanceof Boolean flag) {
                                    existingSession.setIsVpnProxy(flag);
                                }
                                break;
                            case "asnDatacenterFlag":
                                if (value instanceof Boolean flag) {
                                    existingSession.setAsnDatacenterFlag(flag);
                                }
                                break;
                            case "geoCountry":
                                existingSession.setGeoCountry((String) value);
                                break;
                        }
                    });
                    Sessions patchedSession = sessionsRepository.save(existingSession);
                    return ResponseEntity.ok(patchedSession);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete session by ID", description = "Deletes a session entry by session_id")
    public ResponseEntity<Void> deleteSession(@PathVariable("id") Long id) {
        if (sessionsRepository.existsById(id)) {
            sessionsRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}