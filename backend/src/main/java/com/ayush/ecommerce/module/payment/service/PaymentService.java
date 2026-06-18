package com.ayush.ecommerce.module.payment.service;

import com.ayush.ecommerce.module.payment.dto.CreatePaymentRequest;
import com.ayush.ecommerce.module.payment.dto.PaymentResponse;

import java.util.List;

public interface PaymentService {

    PaymentResponse createPayment(
            String userEmail,
            CreatePaymentRequest request
    );

    PaymentResponse getPaymentByReference(
            String userEmail,
            String paymentReference
    );

    List<PaymentResponse> getMyPayments(
            String userEmail
    );
}