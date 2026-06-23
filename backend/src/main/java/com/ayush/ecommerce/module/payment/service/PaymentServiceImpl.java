package com.ayush.ecommerce.module.payment.service;

import com.ayush.ecommerce.exception.OrderNotFoundException;
import com.ayush.ecommerce.exception.PaymentAlreadyExistsException;
import com.ayush.ecommerce.exception.PaymentNotFoundException;
import com.ayush.ecommerce.module.notification.service.EmailService;
import com.ayush.ecommerce.module.order.entity.Order;
import com.ayush.ecommerce.module.order.entity.OrderItem;
import com.ayush.ecommerce.module.order.entity.OrderStatus;
import com.ayush.ecommerce.module.payment.config.RazorpayProperties;
import com.ayush.ecommerce.module.payment.dto.CreatePaymentRequest;
import com.ayush.ecommerce.module.payment.dto.CreatePaymentResponse;
import com.ayush.ecommerce.module.payment.dto.PaymentResponse;
import com.ayush.ecommerce.module.payment.dto.VerifyPaymentRequest;
import com.ayush.ecommerce.module.payment.entity.Payment;
import com.ayush.ecommerce.module.payment.entity.PaymentStatus;
import com.ayush.ecommerce.module.payment.repository.PaymentRepository;
import com.ayush.ecommerce.module.order.repository.OrderRepository;
import com.ayush.ecommerce.module.product.entity.Product;
import com.ayush.ecommerce.module.product.repository.ProductRepository;
import com.razorpay.RazorpayClient;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;



import com.razorpay.Utils;
import org.json.JSONObject;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final RazorpayClient razorpayClient;
    private final RazorpayProperties razorpayProperties;
    private final ProductRepository productRepository;
    private final EmailService emailService;

    @Override
    @Transactional
    public PaymentResponse createPayment(
            String userEmail,
            CreatePaymentRequest request
    ) {

        Order order = orderRepository
                .findById(request.getOrderId())
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
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Payment savedPayment =
                paymentRepository.save(payment);


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

    @Override
    @Transactional
    public CreatePaymentResponse createRazorpayOrder(String userEmail, CreatePaymentRequest request) {
        Order order = orderRepository
                .findById(request.getOrderId())
                .orElseThrow(() ->
                        new OrderNotFoundException(
                                "Order not found"
                        ));

        if (!order.getUser().getEmail().equals(userEmail)) {
            throw new OrderNotFoundException(
                    "Order not found"
            );
        }

        paymentRepository
                .findByOrderId(order.getId())
                .ifPresent(payment -> {
                    throw new PaymentAlreadyExistsException(
                            "Payment already exists"
                    );
                });

        try {

            JSONObject options = new JSONObject();

            options.put(
                    "amount",
                    order.getTotalAmount()
                            .multiply(java.math.BigDecimal.valueOf(100))
                            .longValue()
            );

            options.put(
                    "currency",
                    "INR"
            );

            options.put(
                    "receipt",
                    order.getOrderNumber()
            );

            com.razorpay.Order razorpayOrder =
                    razorpayClient.orders.create(options);

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
                    .status(PaymentStatus.PENDING)
                    .razorpayOrderId(
                            razorpayOrder.get("id")
                                    .toString()
                    )
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            paymentRepository.save(payment);

            return CreatePaymentResponse.builder()
                    .razorpayOrderId(
                            razorpayOrder.get("id")
                                    .toString()
                    )
                    .paymentReference(
                            paymentReference
                    )
                    .amount(
                            order.getTotalAmount()
                                    .multiply(
                                            java.math.BigDecimal.valueOf(100)
                                    )
                                    .longValue()
                    )
                    .currency("INR")
                    .keyId(
                            razorpayProperties.getKeyId()
                    )
                    .build();

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to create Razorpay order",
                    e
            );
        }
    }

    @Override
    @Transactional
    public void verifyPayment(
            VerifyPaymentRequest request
    ) {

        try {

            JSONObject attributes = new JSONObject();

            attributes.put(
                    "razorpay_order_id",
                    request.getRazorpayOrderId()
            );

            attributes.put(
                    "razorpay_payment_id",
                    request.getRazorpayPaymentId()
            );

            attributes.put(
                    "razorpay_signature",
                    request.getRazorpaySignature()
            );

            boolean valid =
                    Utils.verifyPaymentSignature(
                            attributes,
                            razorpayProperties.getKeySecret()
                    );

            if (!valid) {
                throw new RuntimeException(
                        "Invalid payment signature"
                );
            }

            Payment payment =
                    paymentRepository
                            .findByRazorpayOrderId(
                                    request.getRazorpayOrderId()
                            )
                            .orElseThrow(() ->
                                    new PaymentNotFoundException(
                                            "Payment not found"
                                    )
                            );

            if (payment.getStatus() == PaymentStatus.SUCCESS) {
                return;
            }

            processSuccessfulPayment(
                    payment,
                    request.getRazorpayPaymentId(),
                    request.getRazorpaySignature()
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Payment verification failed",
                    e
            );
        }
    }

    private void processSuccessfulPayment(
            Payment payment,
            String razorpayPaymentId,
            String razorpaySignature
    ){
        payment.setRazorpayPaymentId(
                razorpayPaymentId
        );

        payment.setRazorpaySignature(
                razorpaySignature
        );

        payment.setStatus(
                PaymentStatus.SUCCESS
        );
        payment.setUpdatedAt(
                LocalDateTime.now()
        );

        paymentRepository.save(payment);

        Order order = payment.getOrder();

        order.setStatus(
                OrderStatus.PAID
        );
        order.setUpdatedAt(
                LocalDateTime.now()
        );

        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new IllegalStateException(
                    "Order items not found"
            );
        }

        orderRepository.save(order);

        emailService.sendEmail(
                order.getUser().getEmail(),
                "Payment Successful",
                """
                Hello %s,
        
                Your payment has been successfully received.
        
                Order Number: %s
                Payment Reference: %s
                Amount: ₹%s
        
                Thank you for shopping with us.
        
                Team E-Commerce
                """
                        .formatted(
                                order.getUser().getName(),
                                order.getOrderNumber(),
                                payment.getPaymentReference(),
                                payment.getAmount()
                        )
        );

    }

}