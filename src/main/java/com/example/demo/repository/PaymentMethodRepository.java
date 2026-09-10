package com.example.demo.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.PaymentMethods;

public interface PaymentMethodRepository
        extends JpaRepository<PaymentMethods, String> {

    List<PaymentMethods> findByUserId(UUID userId);

    List<PaymentMethods> findByCardStatus(String cardStatus);

    List<PaymentMethods> findByBinNumber(String binNumber);
}