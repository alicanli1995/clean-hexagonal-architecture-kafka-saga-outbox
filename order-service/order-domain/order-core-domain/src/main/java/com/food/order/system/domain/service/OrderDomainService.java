package com.food.order.system.domain.service;

import com.food.order.system.domain.entity.Order;
import com.food.order.system.domain.entity.Restaurant;
import com.food.order.system.domain.event.OrderCancelledEvent;
import com.food.order.system.domain.event.OrderCreatedEvent;
import com.food.order.system.domain.event.OrderPaidEvent;
import com.food.order.sysyem.event.publisher.DomainEventPublisher;

import java.util.List;

public interface OrderDomainService {

    OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant, DomainEventPublisher<OrderCreatedEvent> publisher);

    OrderPaidEvent payOrder(Order order,DomainEventPublisher<OrderPaidEvent> publisher);

    void approve(Order order);

    OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages,DomainEventPublisher<OrderCancelledEvent> publisher);

    void cancelOrder(Order order, List<String> failureMessages);

}
