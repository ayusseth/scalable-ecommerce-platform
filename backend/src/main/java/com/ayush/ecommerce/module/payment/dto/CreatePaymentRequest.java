package com.ayush.ecommerce.module.payment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreatePaymentRequest {

    @NotBlank
    private String orderNumber;
}