package com.ayush.ecommerce.module.cart.service;

import com.ayush.ecommerce.exception.CartNotFoundException;
import com.ayush.ecommerce.exception.ProductNotFoundException;
import com.ayush.ecommerce.exception.UserNotFoundException;
import com.ayush.ecommerce.module.auth.entity.User;
import com.ayush.ecommerce.module.cart.dto.AddToCartRequest;
import com.ayush.ecommerce.module.cart.dto.CartItemResponse;
import com.ayush.ecommerce.module.cart.dto.CartResponse;
import com.ayush.ecommerce.module.cart.entity.Cart;
import com.ayush.ecommerce.module.cart.entity.CartItem;
import com.ayush.ecommerce.module.cart.repository.CartItemRepository;
import com.ayush.ecommerce.module.cart.repository.CartRepository;
import com.ayush.ecommerce.module.auth.repository.UserRepository;
import com.ayush.ecommerce.module.product.entity.Product;
import com.ayush.ecommerce.module.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    @Override
    public CartResponse addToCart(
            String userEmail,
            AddToCartRequest request
    ) {

        User user = userRepository
                .findByEmail(userEmail)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found"
                        ));

        Product product = productRepository
                .findByIdAndActiveTrue(
                        request.getProductId()
                )
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found"
                        ));

        Cart cart = cartRepository
                .findByUserEmail(userEmail)
                .orElseGet(() -> {

                    Cart newCart = Cart.builder()
                            .user(user)
                            .build();

                    return cartRepository.save(newCart);
                });

        CartItem cartItem = cartItemRepository
                .findByCartIdAndProductId(
                        cart.getId(),
                        product.getId()
                )
                .orElseGet(() ->
                        CartItem.builder()
                                .cart(cart)
                                .product(product)
                                .quantity(0)
                                .build()
                );

        cartItem.setQuantity(
                cartItem.getQuantity()
                        + request.getQuantity()
        );

        cartItemRepository.save(cartItem);

        return getCart(userEmail);
    }

    @Transactional(readOnly = true)
    @Override
    public CartResponse getCart(
            String userEmail
    ) {

        Cart cart = cartRepository
                .findByUserEmail(userEmail)
                .orElseThrow(() ->
                        new CartNotFoundException(
                                "Cart not found"
                        ));

        List<CartItemResponse> items =
                cartItemRepository
                        .findByCartId(cart.getId())
                        .stream()
                        .map(item -> {

                            BigDecimal subtotal =
                                    item.getProduct()
                                            .getPrice()
                                            .multiply(
                                                    BigDecimal.valueOf(
                                                            item.getQuantity()
                                                    )
                                            );

                            return CartItemResponse
                                    .builder()
                                    .productId(
                                            item.getProduct().getId()
                                    )
                                    .productName(
                                            item.getProduct().getName()
                                    )
                                    .quantity(
                                            item.getQuantity()
                                    )
                                    .unitPrice(
                                            item.getProduct().getPrice()
                                    )
                                    .subtotal(subtotal)
                                    .build();
                        })
                        .toList();

        BigDecimal totalAmount = items.stream()
                .map(CartItemResponse::getSubtotal)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );

        return CartResponse.builder()
                .cartId(cart.getId())
                .items(items)
                .totalAmount(totalAmount)
                .build();
    }

    @Override
    @Transactional
    public CartResponse updateCartItem(
            String userEmail,
            Long productId,
            Integer quantity
    ) {

        Cart cart = cartRepository
                .findByUserEmail(userEmail)
                .orElseThrow(() ->
                        new CartNotFoundException(
                                "Cart not found"
                        ));

        CartItem item = cartItemRepository
                .findByCartIdAndProductId(
                        cart.getId(),
                        productId
                )
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found in cart"
                        ));

        item.setQuantity(quantity);

        cartItemRepository.save(item);

        return getCart(userEmail);
    }

    @Override
    @Transactional
    public void removeCartItem(
            String userEmail,
            Long productId
    ) {

        Cart cart = cartRepository
                .findByUserEmail(userEmail)
                .orElseThrow(() ->
                        new CartNotFoundException(
                                "Cart not found"
                        ));

        cartItemRepository.deleteByCartIdAndProductId(
                cart.getId(),
                productId
        );
    }

    @Override
    @Transactional
    public void clearCart(
            String userEmail
    ) {

        Cart cart = cartRepository
                .findByUserEmail(userEmail)
                .orElseThrow(() ->
                        new CartNotFoundException(
                                "Cart not found"
                        ));

        cartItemRepository.deleteByCartId(
                cart.getId()
        );
    }
}