package com.ayush.ecommerce.module.notification.controller;

import com.ayush.ecommerce.module.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailTestController {

    private final EmailService emailService;

    @GetMapping("/api/v1/test/email")
    public String sendTestEmail(
            @RequestParam String to
    ) {

        emailService.sendEmail(
                to,
                "Ecommerce Test Email",
                "Congratulations! Email service is working successfully."
        );

        return "Email sent successfully";
    }
}