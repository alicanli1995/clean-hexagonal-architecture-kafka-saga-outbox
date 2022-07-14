package com.food.order.system.restaurant.domain.core.event;

import com.food.order.system.restaurant.domain.core.entity.OrderApproval;
import com.food.order.sysyem.event.publisher.DomainEventPublisher;
import com.food.order.sysyem.valueobject.RestaurantId;

import java.time.ZonedDateTime;
import java.util.List;

public class OrderRejectedEvent extends OrderApprovalEvent {

    private final DomainEventPublisher<OrderRejectedEvent> publisher;

    public OrderRejectedEvent(OrderApproval orderApproval,
                              RestaurantId restaurantId,
                              List<String> failureMessages,
                              ZonedDateTime createdAt,
                              DomainEventPublisher<OrderRejectedEvent> publisher) {
        super(orderApproval, restaurantId, failureMessages, createdAt);
        this.publisher = publisher;
    }

    @Override
    public void fire() {
        publisher.publish(this);
    }
}
