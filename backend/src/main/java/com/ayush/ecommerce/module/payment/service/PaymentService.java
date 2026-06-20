package com.ayush.ecommerce.module.payment.service;

import com.ayush.ecommerce.module.payment.dto.CreatePaymentRequest;
import com.ayush.ecommerce.module.payment.dto.CreatePaymentResponse;
import com.ayush.ecommerce.module.payment.dto.PaymentResponse;
import com.ayush.ecommerce.module.payment.dto.VerifyPaymentRequest;

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
    CreatePaymentResponse createRazorpayOrder(
            String userEmail,
            CreatePaymentRequest request
    );

    void verifyPayment(
            VerifyPaymentRequest request
    );
}