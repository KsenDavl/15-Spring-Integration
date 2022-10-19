package ru.otus.spring.davlks.springintegration.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.davlks.springintegration.domain.Order;

@Service
public class PaymentService {

    public Order getPayment(Order order) {
        System.out.println("Payment is complete!");
        return order;
    }
}
