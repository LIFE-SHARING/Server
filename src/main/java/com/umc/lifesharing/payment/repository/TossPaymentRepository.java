package com.umc.lifesharing.payment.repository;

import com.umc.lifesharing.payment.entity.TossPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TossPaymentRepository extends JpaRepository<TossPayment, Long> {
    Optional<TossPayment> findByOrderId(String orderId);
    Optional<TossPayment> findByPaymentKeyAndUser_Email(String paymentKey, String email);
    Page<TossPayment> findAllByUser_Email(String email, PageRequest page);
}