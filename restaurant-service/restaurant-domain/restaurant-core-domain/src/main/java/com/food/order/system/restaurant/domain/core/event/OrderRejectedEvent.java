package com.food.order.system.restaurant.domain.core.event;

import com.food.order.system.restaurant.domain.core.entity.OrderApproval;
import com.food.order.system.event.publisher.DomainEventPublisher;
import com.food.order.system.valueobject.RestaurantId;

import java.time.ZonedDateTime;
import java.util.List;

public class OrderRejectedEvent extends OrderApprovalEvent {


    public OrderRejectedEvent(OrderApproval orderApproval,
                              RestaurantId restaurantId,
                              List<String> failureMessages,
                              ZonedDateTime createdAt) {
        super(orderApproval, restaurantId, failureMessages, createdAt);
    }
}
