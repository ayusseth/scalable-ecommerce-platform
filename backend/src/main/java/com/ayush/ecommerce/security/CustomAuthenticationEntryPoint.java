package com.ayush.ecommerce.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class CustomAuthenticationEntryPoint
        implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) {

        authException.printStackTrace();   // <-- ADD THIS

        try {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");

            Map<String, Object> body = Map.of(
                    "status", 401,
                    "message", authException.getMessage(), // <-- CHANGE THIS
                    "timestamp", LocalDateTime.now().toString()
            );

            new ObjectMapper().writeValue(response.getOutputStream(), body);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}