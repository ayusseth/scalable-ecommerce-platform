package com.ayush.ecommerce.module.payment.service;

import com.ayush.ecommerce.module.payment.dto.CreatePaymentRequest;
import com.ayush.ecommerce.module.payment.dto.PaymentResponse;

public interface PaymentService {

    PaymentResponse createPayment(
            String userEmail,
            CreatePaymentRequest request
    );
}