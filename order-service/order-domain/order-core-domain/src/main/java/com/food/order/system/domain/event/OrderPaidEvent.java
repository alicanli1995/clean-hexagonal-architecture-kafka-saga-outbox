package com.food.order.system.domain.event;

import com.food.order.system.domain.entity.Order;
import com.food.order.sysyem.event.publisher.DomainEventPublisher;

import java.time.ZonedDateTime;

public class OrderPaidEvent extends OrderEvent {

    private final DomainEventPublisher<OrderPaidEvent> publisher;

    public OrderPaidEvent(Order order, ZonedDateTime utc, DomainEventPublisher<OrderPaidEvent> publisher) {
        super(order, utc);
        this.publisher = publisher;
    }

    @Override
    public void fire() {
        publisher.publish(this);
    }
}
