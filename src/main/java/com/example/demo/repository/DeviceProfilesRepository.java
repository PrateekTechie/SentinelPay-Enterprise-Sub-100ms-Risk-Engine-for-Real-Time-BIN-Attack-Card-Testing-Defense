package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.DeviceProfiles;

@Repository
public interface DeviceProfilesRepository extends JpaRepository<DeviceProfiles, Long> {

    List<DeviceProfiles> findByDeviceFingerprint(String deviceFingerprint);

    List<DeviceProfiles> findByProfileStatus(String profileStatus);

    List<DeviceProfiles> findByIpAddress(String ipAddress);
}