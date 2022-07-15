package com.food.order.system.domain.event;

import com.food.order.system.domain.entity.Order;
import com.food.order.sysyem.event.publisher.DomainEventPublisher;

import java.time.ZonedDateTime;

public class OrderPaidEvent extends OrderEvent {


    public OrderPaidEvent(Order order, ZonedDateTime utc) {
        super(order, utc);
    }


}
