package com.ayush.ecommerce.module.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl
        implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(
            String to,
            String subject,
            String body
    ) {

        try {

            System.out.println("Sending email to: " + to);

            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            throw new RuntimeException("EMAIL SERVICE REACHED");

            

        } catch (Exception e) {

            System.out.println("EMAIL ERROR:");
            e.printStackTrace();

            throw e;
        }
    }
}