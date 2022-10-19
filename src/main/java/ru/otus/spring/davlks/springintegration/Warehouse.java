package ru.otus.spring.davlks.springintegration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.spring.davlks.springintegration.domain.Order;

@MessagingGateway
public interface Warehouse {

    @Gateway(requestChannel = "orderChannel", replyChannel = "customerChannel")
    Order process(Order order);

}
