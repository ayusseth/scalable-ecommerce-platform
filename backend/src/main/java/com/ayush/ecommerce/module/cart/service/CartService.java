package com.ayush.ecommerce.module.cart.service;

import com.ayush.ecommerce.module.cart.dto.AddToCartRequest;
import com.ayush.ecommerce.module.cart.dto.CartResponse;

public interface CartService {

    CartResponse addToCart(
            String userEmail,
            AddToCartRequest request
    );

    CartResponse getCart(
            String userEmail
    );
}