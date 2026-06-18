package com.ayush.ecommerce.module.payment.dto;

import com.ayush.ecommerce.module.payment.entity.PaymentStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class PaymentResponse {

    private Long paymentId;

    private String paymentReference;

    private String orderNumber;

    private BigDecimal amount;

    private PaymentStatus status;
}