package com.example.demo.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Accounts;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, UUID> {

    List<Accounts> findByKycStatus(String kycStatus);

    List<Accounts> findByHomeZip(String homeZip);

    List<Accounts> findByIsSuspended(Boolean isSuspended);
}