package com.ayush.ecommerce.module.payment.service;

import com.ayush.ecommerce.module.notification.service.EmailService;
import com.ayush.ecommerce.module.order.entity.Order;
import com.ayush.ecommerce.module.order.entity.OrderStatus;
import com.ayush.ecommerce.module.order.repository.OrderRepository;
import com.ayush.ecommerce.module.payment.entity.Payment;
import com.ayush.ecommerce.module.payment.entity.PaymentStatus;
import com.ayush.ecommerce.module.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RazorpayWebhookServiceImpl
        implements RazorpayWebhookService {

    private final PaymentRepository paymentRepository;
    private final EmailService emailService;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public void processWebhook(
            String payload
    ) {

        JSONObject json =
                new JSONObject(payload);

        String event =
                json.getString("event");

        log.info(
                "Webhook Event={}",
                event
        );

        if (!"payment.captured".equals(event)) {
            return;
        }

        JSONObject paymentEntity =
                json.getJSONObject("payload")
                        .getJSONObject("payment")
                        .getJSONObject("entity");

        String paymentId =
                paymentEntity.getString("id");

        String razorpayOrderId =
                paymentEntity.getString("order_id");

        Payment payment =
                paymentRepository
                        .findByRazorpayOrderId(
                                razorpayOrderId
                        )
                        .orElse(null);

        if (payment == null) {

            log.warn(
                    "Payment not found for Razorpay Order={}",
                    razorpayOrderId
            );

            return;
        }

        if (payment.getStatus() == PaymentStatus.SUCCESS) {

            log.info(
                    "Payment already processed. Reference={}",
                    payment.getPaymentReference()
            );

            return;
        }

        payment.setStatus(
                PaymentStatus.SUCCESS
        );

        payment.setRazorpayPaymentId(
                paymentId
        );

        payment.setUpdatedAt(
                java.time.LocalDateTime.now()
        );

        Order order = payment.getOrder();

        order.setStatus(
                OrderStatus.PAID
        );

        order.setUpdatedAt(
                java.time.LocalDateTime.now()
        );

        orderRepository.save(order);

        paymentRepository.save(payment);

        emailService.sendEmail(
                order.getUser().getEmail(),
                "Payment Successful",
                """
                Hello %s,
    
                Your payment has been received.
    
                Order Number: %s
                Payment Reference: %s
    
                Thank you for shopping with us.
                """
                        .formatted(
                                order.getUser().getName(),
                                order.getOrderNumber(),
                                payment.getPaymentReference()
                        )
        );

        log.info(
                "Webhook processed successfully. PaymentReference={}",
                payment.getPaymentReference()
        );
    }
}