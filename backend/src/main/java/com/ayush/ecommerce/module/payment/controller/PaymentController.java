package com.ayush.ecommerce.module.payment.controller;

import com.ayush.ecommerce.module.payment.dto.CreatePaymentRequest;
import com.ayush.ecommerce.module.payment.dto.CreatePaymentResponse;
import com.ayush.ecommerce.module.payment.dto.PaymentResponse;
import com.ayush.ecommerce.module.payment.dto.VerifyPaymentRequest;
import com.ayush.ecommerce.module.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public PaymentResponse createPayment(
            Authentication authentication,
            @Valid @RequestBody CreatePaymentRequest request
    ) {

        return paymentService.createPayment(
                authentication.getName(),
                request
        );
    }

    @GetMapping("/{paymentReference}")
    public PaymentResponse getPayment(
            Authentication authentication,
            @PathVariable String paymentReference
    ){
        return paymentService.getPaymentByReference(
                authentication.getName(),
                paymentReference
        );
    }

    @GetMapping("/my-payments")
    public List<PaymentResponse> getMyPayments(
            Authentication authentication
    ){
        return paymentService.getMyPayments(
                authentication.getName()
        );
    }

    @PostMapping("/razorpay/create-order")
    public CreatePaymentResponse createRazorpayOrder(Authentication authentication,
                                                     @Valid @RequestBody CreatePaymentRequest request){
        return paymentService.createRazorpayOrder(authentication.getName(), request);
    }

    @PostMapping("/razorpay/verify")
    public String verifyPayment(
            @Valid
            @RequestBody
            VerifyPaymentRequest request
    ){
        paymentService.verifyPayment(request);

        return "Payment verified successfully";
    }
}