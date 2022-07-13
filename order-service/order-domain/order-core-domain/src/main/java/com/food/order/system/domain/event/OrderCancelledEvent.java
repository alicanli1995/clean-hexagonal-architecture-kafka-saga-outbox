package com.food.order.system.domain.event;

import com.food.order.system.domain.entity.Order;
import com.food.order.sysyem.event.publisher.DomainEventPublisher;

import java.time.ZonedDateTime;

public class OrderCancelledEvent extends OrderEvent {

    private final DomainEventPublisher<OrderCancelledEvent> publisher;


    public OrderCancelledEvent(Order order, ZonedDateTime utc, DomainEventPublisher<OrderCancelledEvent> publisher) {
        super(order, utc);
        this.publisher = publisher;
    }

    @Override
    public void fire() {
        publisher.publish(this);
    }
}
