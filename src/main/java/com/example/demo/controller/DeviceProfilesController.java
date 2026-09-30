package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.DeviceProfiles;
import com.example.demo.repository.DeviceProfilesRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/device-profiles")
@Tag(name = "Device Profiles Controller", description = "APIs for managing device profiles")
public class DeviceProfilesController {

    @Autowired
    private DeviceProfilesRepository deviceProfilesRepository;

    @GetMapping
    @Operation(summary = "Get all device profiles")
    public ResponseEntity<List<DeviceProfiles>> getAllDeviceProfiles() {
        List<DeviceProfiles> profiles = deviceProfilesRepository.findAll();
        return ResponseEntity.ok(profiles);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get device profile by Profile ID")
    public ResponseEntity<DeviceProfiles> getDeviceProfileById(@PathVariable("id") Long profileId) {
        return deviceProfilesRepository.findById(profileId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/fingerprint/{fingerprint}")
    @Operation(summary = "Get device profiles by Device Fingerprint")
    public ResponseEntity<List<DeviceProfiles>> getDeviceProfilesByFingerprint(@PathVariable("fingerprint") String fingerprint) {
        List<DeviceProfiles> profiles = deviceProfilesRepository.findByDeviceFingerprint(fingerprint);
        return ResponseEntity.ok(profiles);
    }

    @GetMapping("/status")
    @Operation(summary = "Get device profiles by Profile Status")
    public ResponseEntity<List<DeviceProfiles>> getDeviceProfilesByStatus(@RequestParam String status) {
        List<DeviceProfiles> profiles = deviceProfilesRepository.findByProfileStatus(status);
        return ResponseEntity.ok(profiles);
    }

    @PostMapping
    @Operation(summary = "Create a new device profile")
    public ResponseEntity<DeviceProfiles> createDeviceProfile(@Valid @RequestBody DeviceProfiles deviceProfile) {
        DeviceProfiles savedProfile = deviceProfilesRepository.save(deviceProfile);
        return new ResponseEntity<>(savedProfile, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing device profile")
    public ResponseEntity<DeviceProfiles> updateDeviceProfile(@PathVariable("id") Long profileId, 
                                                               @Valid @RequestBody DeviceProfiles profileDetails) {
        return deviceProfilesRepository.findById(profileId)
                .map(existingProfile -> {
                    existingProfile.setDeviceFingerprint(profileDetails.getDeviceFingerprint());
                    existingProfile.setUpdatedAt(LocalDateTime.now());
                    existingProfile.setBehavioralRiskScore(profileDetails.getBehavioralRiskScore());
                    existingProfile.setIpAddress(profileDetails.getIpAddress());
                    existingProfile.setOsVersion(profileDetails.getOsVersion());
                    existingProfile.setAppVersion(profileDetails.getAppVersion());
                    existingProfile.setProfileStatus(profileDetails.getProfileStatus());
                    DeviceProfiles updatedProfile = deviceProfilesRepository.save(existingProfile);
                    return ResponseEntity.ok(updatedProfile);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update a device profile", description = "Updates specific fields of a device profile (e.g., risk score, status, or IP address)")
    public ResponseEntity<DeviceProfiles> patchDeviceProfile(@PathVariable("id") Long profileId,
                                                              @RequestBody Map<String, Object> updates) {
        return deviceProfilesRepository.findById(profileId)
                .map(existingProfile -> {
                    updates.forEach((key, value) -> {
                        switch (key) {
                            case "deviceFingerprint":
                                existingProfile.setDeviceFingerprint((String) value);
                                break;
                            case "behavioralRiskScore":
                                if (value instanceof Number) {
                                    existingProfile.setBehavioralRiskScore(new java.math.BigDecimal(value.toString()));
                                }
                                break;
                            case "ipAddress":
                                existingProfile.setIpAddress((String) value);
                                break;
                            case "osVersion":
                                existingProfile.setOsVersion((String) value);
                                break;
                            case "appVersion":
                                existingProfile.setAppVersion((String) value);
                                break;
                            case "profileStatus":
                                existingProfile.setProfileStatus((String) value);
                                break;
                        }
                    });
                    existingProfile.setUpdatedAt(LocalDateTime.now());
                    DeviceProfiles patchedProfile = deviceProfilesRepository.save(existingProfile);
                    return ResponseEntity.ok(patchedProfile);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a device profile by Profile ID")
    public ResponseEntity<Void> deleteDeviceProfile(@PathVariable("id") Long profileId) {
        return deviceProfilesRepository.findById(profileId)
                .map(profile -> {
                    deviceProfilesRepository.delete(profile);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}