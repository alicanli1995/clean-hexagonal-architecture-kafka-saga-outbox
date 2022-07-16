package com.food.order.system.restaurant.domain.core.event;

import com.food.order.system.restaurant.domain.core.entity.OrderApproval;
import com.food.order.system.event.publisher.DomainEventPublisher;
import com.food.order.system.valueobject.RestaurantId;

import java.time.ZonedDateTime;
import java.util.List;

public class OrderApprovedEvent extends OrderApprovalEvent {


    private final DomainEventPublisher<OrderApprovedEvent> publisher;

    public OrderApprovedEvent(OrderApproval orderApproval,
                              RestaurantId restaurantId,
                              List<String> failureMessages,
                              ZonedDateTime createdAt,
                              DomainEventPublisher<OrderApprovedEvent> publisher) {
        super(orderApproval, restaurantId, failureMessages, createdAt);
        this.publisher = publisher;
    }

    @Override
    public void fire() {
        publisher.publish(this);
    }
}
