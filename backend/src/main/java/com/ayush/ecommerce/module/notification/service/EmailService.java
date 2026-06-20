package com.ayush.ecommerce.module.notification.service;

public interface EmailService {

    void sendEmail(
            String to,
            String subject,
            String body
    );
}