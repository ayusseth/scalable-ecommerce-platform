package com.ayush.ecommerce.module.payment.repository;

import com.ayush.ecommerce.module.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

    Optional<Payment> findByPaymentReference(
            String paymentReference
    );

    Optional<Payment> findByOrderId(
            Long orderId
    );

    Optional<Payment> findByOrderOrderNumber(
            String orderNumber
    );
    List<Payment> findByOrderUserEmailOrderByCreatedAtDesc(
            String email
    );

    Optional<Payment> findByRazorpayOrderId(
            String razorpayOrderId
    );

}