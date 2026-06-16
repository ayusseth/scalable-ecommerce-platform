package com.ayush.ecommerce.module.admin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    @GetMapping("/api/v1/admin/dashboard")
    public String dashboard(){
        return "Welcome Admin";
    }
}
