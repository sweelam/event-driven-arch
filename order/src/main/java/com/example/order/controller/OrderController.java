package com.example.order.controller;

import com.example.order.dto.Order;
import com.example.order.service.BillingService;
import com.example.order.service.EmailService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    private final EmailService emailService;
    private final BillingService billingService;

    public OrderController(EmailService emailService, BillingService billingService) {
        this.emailService = emailService;
        this.billingService = billingService;
    }

    @PostMapping("/order")
    public void submitOrder(@RequestBody Order order) {

        billingService.charge(order);
        emailService.sendEmail(order);

    }

}
