package com.ayush.ecommerce.module.payment.service;

public interface RazorpayWebhookService {

    void processWebhook(String payload);

}