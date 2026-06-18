package com.ayush.ecommerce.module.payment.service;

import com.ayush.ecommerce.exception.OrderNotFoundException;
import com.ayush.ecommerce.exception.PaymentAlreadyExistsException;
import com.ayush.ecommerce.exception.PaymentNotFoundException;
import com.ayush.ecommerce.module.order.entity.Order;
import com.ayush.ecommerce.module.order.entity.OrderStatus;
import com.ayush.ecommerce.module.payment.dto.CreatePaymentRequest;
import com.ayush.ecommerce.module.payment.dto.PaymentResponse;
import com.ayush.ecommerce.module.payment.entity.Payment;
import com.ayush.ecommerce.module.payment.entity.PaymentStatus;
import com.ayush.ecommerce.module.payment.repository.PaymentRepository;
import com.ayush.ecommerce.module.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public PaymentResponse createPayment(
            String userEmail,
            CreatePaymentRequest request
    ) {

        Order order = orderRepository
                .findByOrderNumber(request.getOrderNumber())
                .orElseThrow(() ->
                        new OrderNotFoundException(
                                "Order not found"
                        ));

        if (!order.getUser().getEmail().equals(userEmail)) {
            throw new OrderNotFoundException(
                    "Order not found"
            );
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException(
                    "Payment can only be created for PENDING orders"
            );
        }

        paymentRepository
                .findByOrderId(order.getId())
                .ifPresent(payment -> {
                    throw new PaymentAlreadyExistsException(
                            "Payment already exists for this order"
                    );
                });

        String paymentReference =
                "PAY-" +
                        UUID.randomUUID()
                                .toString()
                                .substring(0, 8)
                                .toUpperCase();

        Payment payment = Payment.builder()
                .paymentReference(paymentReference)
                .order(order)
                .amount(order.getTotalAmount())
                .status(PaymentStatus.SUCCESS)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Payment savedPayment =
                paymentRepository.save(payment);

        order.setStatus(OrderStatus.PAID);
        order.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(order);

        return PaymentResponse.builder()
                .paymentId(savedPayment.getId())
                .paymentReference(savedPayment.getPaymentReference())
                .orderNumber(order.getOrderNumber())
                .amount(savedPayment.getAmount())
                .status(savedPayment.getStatus())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByReference(
            String userEmail,
            String paymentReference
    ) {

        Payment payment = paymentRepository
                .findByPaymentReference(paymentReference)
                .orElseThrow(() ->
                        new PaymentNotFoundException(
                                "Payment not found"
                        ));

        if (!payment.getOrder()
                .getUser()
                .getEmail()
                .equals(userEmail)) {

            throw new PaymentNotFoundException(
                    "Payment not found"
            );
        }

        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .paymentReference(payment.getPaymentReference())
                .orderNumber(
                        payment.getOrder()
                                .getOrderNumber()
                )
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getMyPayments(
            String userEmail
    ) {

        return paymentRepository
                .findByOrderUserEmailOrderByCreatedAtDesc(
                        userEmail
                )
                .stream()
                .map(payment ->
                        PaymentResponse.builder()
                                .paymentId(payment.getId())
                                .paymentReference(
                                        payment.getPaymentReference()
                                )
                                .orderNumber(
                                        payment.getOrder()
                                                .getOrderNumber()
                                )
                                .amount(payment.getAmount())
                                .status(payment.getStatus())
                                .build()
                )
                .toList();
    }
}