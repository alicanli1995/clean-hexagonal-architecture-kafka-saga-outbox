package com.food.order.system.restaurant.domain.core;

import com.food.order.system.restaurant.domain.core.entity.Restaurant;
import com.food.order.system.restaurant.domain.core.event.OrderApprovalEvent;
import com.food.order.system.restaurant.domain.core.event.OrderApprovedEvent;
import com.food.order.system.restaurant.domain.core.event.OrderRejectedEvent;
import com.food.order.sysyem.event.publisher.DomainEventPublisher;

import java.util.List;

public interface RestaurantDomainService {
    OrderApprovalEvent validateOrder(Restaurant restaurant,
                                     List<String> failureMessages,
                                     DomainEventPublisher<OrderApprovedEvent> publisher,
                                     DomainEventPublisher<OrderRejectedEvent> rejectedPublisher);

}
