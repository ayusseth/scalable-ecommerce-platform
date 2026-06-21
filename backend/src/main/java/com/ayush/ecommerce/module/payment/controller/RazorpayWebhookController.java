package com.ayush.ecommerce.module.payment.controller;

import com.ayush.ecommerce.module.payment.service.RazorpayWebhookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments/razorpay")
@RequiredArgsConstructor
@Slf4j
public class RazorpayWebhookController {

    private final RazorpayWebhookService razorpayWebhookService;

    @PostMapping("/webhook")
    public String handleWebhook(@RequestBody String payload,
                                @RequestHeader(
                                        value = "X-Razorpay-Signature",
                                        required = false
                                )
                                String signature){
        log.info("Webhook received. Signature={}", signature);

        razorpayWebhookService.processWebhook(payload);
        return "Webhook received";
    }
}
