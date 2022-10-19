package ru.otus.spring.davlks.springintegration.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.davlks.springintegration.domain.Order;

@Service
public class DeliveryService {

    public Order deliverOrder(Order order) throws InterruptedException {
        Thread.sleep(3000);
        System.out.println("The order is delivered! Check the content: " + order.getOrderItems());
        return order;
    }
}
