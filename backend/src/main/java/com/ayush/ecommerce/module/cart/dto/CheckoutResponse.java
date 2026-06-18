package com.ayush.ecommerce.module.cart.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CheckoutResponse {

    private String orderNumber;
}