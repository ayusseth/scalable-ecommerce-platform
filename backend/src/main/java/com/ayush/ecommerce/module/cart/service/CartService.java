package com.ayush.ecommerce.module.cart.service;

import com.ayush.ecommerce.module.cart.dto.AddToCartRequest;
import com.ayush.ecommerce.module.cart.dto.CartResponse;
import com.ayush.ecommerce.module.cart.dto.CheckoutResponse;

public interface CartService {

    CartResponse addToCart(
            String userEmail,
            AddToCartRequest request
    );

    CartResponse getCart(
            String userEmail
    );

    CartResponse updateCartItem(
            String userEmail,
            Long productId,
            Integer quantity
    );

    void removeCartItem(
            String userEmail,
            Long productId
    );

    void clearCart(
            String userEmail
    );

    CheckoutResponse checkout(
            String userEmail
    );
}