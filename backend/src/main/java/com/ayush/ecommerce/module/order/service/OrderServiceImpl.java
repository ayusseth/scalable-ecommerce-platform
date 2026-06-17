package com.ayush.ecommerce.module.order.service;

import com.ayush.ecommerce.exception.ProductNotFoundException;
import com.ayush.ecommerce.exception.UserNotFoundException;
import com.ayush.ecommerce.module.auth.entity.User;
import com.ayush.ecommerce.module.auth.repository.UserRepository;
import com.ayush.ecommerce.module.order.dto.CreateOrderRequest;
import com.ayush.ecommerce.module.order.dto.OrderResponse;
import com.ayush.ecommerce.module.order.repository.OrderItemRepository;
import com.ayush.ecommerce.module.order.repository.OrderRepository;
import com.ayush.ecommerce.module.product.entity.Product;
import com.ayush.ecommerce.module.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    @Override
    public OrderResponse createOrder(
            String userEmail,
            CreateOrderRequest request) {
        User user = userRepository
                .findByEmail(userEmail)
                .orElseThrow(()-> new UserNotFoundException("User not found"));
        List<Long> productIds = request.getItems()
                .stream()
                .map(item->item.getProductId())
                .toList();

        List<Product> products = productRepository
                .findByIdInAndActiveTrue(productIds);
        if(products.size()!=productIds.size()){
            throw new ProductNotFoundException("One or more products not found");
        }

        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(
                        Product::getId,
                        Function.identity()
                ));
        return null;
    }
}
