package com.ayush.ecommerce.module.cart.service;

import com.ayush.ecommerce.exception.CartNotFoundException;
import com.ayush.ecommerce.exception.InsufficientStockException;
import com.ayush.ecommerce.exception.ProductNotFoundException;
import com.ayush.ecommerce.exception.UserNotFoundException;
import com.ayush.ecommerce.module.auth.entity.User;
import com.ayush.ecommerce.module.cart.dto.AddToCartRequest;
import com.ayush.ecommerce.module.cart.dto.CartItemResponse;
import com.ayush.ecommerce.module.cart.dto.CartResponse;
import com.ayush.ecommerce.module.cart.dto.CheckoutResponse;
import com.ayush.ecommerce.module.cart.entity.Cart;
import com.ayush.ecommerce.module.cart.entity.CartItem;
import com.ayush.ecommerce.module.cart.repository.CartItemRepository;
import com.ayush.ecommerce.module.cart.repository.CartRepository;
import com.ayush.ecommerce.module.auth.repository.UserRepository;
import com.ayush.ecommerce.module.order.entity.Order;
import com.ayush.ecommerce.module.order.entity.OrderItem;
import com.ayush.ecommerce.module.order.entity.OrderStatus;
import com.ayush.ecommerce.module.order.repository.OrderItemRepository;
import com.ayush.ecommerce.module.order.repository.OrderRepository;
import com.ayush.ecommerce.module.product.entity.Product;
import com.ayush.ecommerce.module.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

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

    @Override
    @Transactional
    public CheckoutResponse checkout(
            String userEmail
    ) {

        User user = userRepository
                .findByEmail(userEmail)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found"
                        ));

        Cart cart = cartRepository
                .findByUserEmail(userEmail)
                .orElseThrow(() ->
                        new CartNotFoundException(
                                "Cart not found"
                        ));

        List<CartItem> cartItems =
                cartItemRepository.findByCartId(
                        cart.getId()
                );

        if (cartItems.isEmpty()) {
            throw new CartNotFoundException(
                    "Cart is empty"
            );
        }

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem item : cartItems) {

            Product product = item.getProduct();

            if (product.getStockQuantity()
                    < item.getQuantity()) {

                throw new InsufficientStockException(
                        "Insufficient stock for "
                                + product.getName()
                );
            }

            totalAmount = totalAmount.add(
                    product.getPrice().multiply(
                            BigDecimal.valueOf(
                                    item.getQuantity()
                            )
                    )
            );
        }

        String orderNumber =
                "ORD-" +
                        UUID.randomUUID()
                                .toString()
                                .substring(0, 8)
                                .toUpperCase();

        Order order = Order.builder()
                .orderNumber(orderNumber)
                .user(user)
                .status(OrderStatus.PENDING)
                .totalAmount(totalAmount)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Order savedOrder =
                orderRepository.save(order);

        for (CartItem item : cartItems) {

            Product product = item.getProduct();

            product.setStockQuantity(
                    product.getStockQuantity()
                            - item.getQuantity()
            );

            productRepository.save(product);

            OrderItem orderItem =
                    OrderItem.builder()
                            .order(savedOrder)
                            .product(product)
                            .quantity(item.getQuantity())
                            .unitPrice(product.getPrice())
                            .subtotal(
                                    product.getPrice()
                                            .multiply(
                                                    BigDecimal.valueOf(
                                                            item.getQuantity()
                                                    )
                                            )
                            )
                            .build();

            orderItemRepository.save(orderItem);
        }

        cartItemRepository.deleteByCartId(
                cart.getId()
        );

        return CheckoutResponse.builder()
                .orderNumber(orderNumber)
                .build();
    }
}