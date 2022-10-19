package ru.otus.spring.davlks.springintegration;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.util.ErrorHandler;
import ru.otus.spring.davlks.springintegration.domain.Order;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

@SpringBootApplication
public class SpringIntegrationApplication {

    static final List<String> POSSIBLE_ORDER_ITEMS = List.of("milk", "bread", "salt", "apple", "cereal", "meat", "rice", "buckwheat", "pasta");

    public static void main(String[] args) throws InterruptedException {
        AbstractApplicationContext ctx = new AnnotationConfigApplicationContext(SpringIntegrationApplication.class);

        Warehouse warehouse = ctx.getBean(Warehouse.class);
        ForkJoinPool pool = ForkJoinPool.commonPool();

        while (true) {
            Thread.sleep( 7000 );

            pool.execute( () -> {
                Order order = generateOrder();
                System.out.println("New order received: " + order.getOrderItems());
                warehouse.process(order);
                System.out.println("The order has been processed!");
            } );
        }
    }

    @Bean
    public QueueChannel orderChannel() {
        return MessageChannels.queue( 10 ).get();
    }

    @Bean
    public PublishSubscribeChannel customerChannel() {
        return MessageChannels.publishSubscribe().errorHandler(new ErrorHandler() {
            @Override
            public void handleError(Throwable t) {
                System.out.println("The order will not be processed!");
            }
        }).get();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller() {
        return Pollers.fixedRate( 100 ).maxMessagesPerPoll( 2 ).get();
    }


    @Bean
    public IntegrationFlow orderFlow() {
        return IntegrationFlows.from( "orderChannel" )
                .handle( "paymentService", "getPayment" )
                .handle("collectingOrderItemsService", "collectItems")
                .handle("deliveryService", "deliverOrder")
                .channel( "customerChannel" )
                .get();
    }

    private static Order generateOrder() {
        List<String> orderItems = new ArrayList<>();

        for (int i = 0; i < 5; i++){
            orderItems.add(POSSIBLE_ORDER_ITEMS.get(RandomUtils.nextInt(0, POSSIBLE_ORDER_ITEMS.size())));
        }

        return new Order(orderItems);
    }

}
