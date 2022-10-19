package ru.otus.spring.davlks.springintegration.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.davlks.springintegration.domain.Order;

@Service
public class CollectingOrderItemsService {

    public Order collectItems(Order order) {
        order.getOrderItems().add("surprise from company");
        System.out.println("The order is collected!");
        return order;
    }
}

