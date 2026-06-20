package com.ayush.ecommerce.module.payment.dto;

import lombok.Builder;

@Builder
public record CreatePaymentResponse(
        String razorpayOrderId,
        String paymentReference,
        Long amount,
        String currency,
        String keyId
) {
}