package com.food.order.system.restaurant.domain.service.ports.output.message.publisher;

import com.food.order.system.restaurant.domain.core.event.OrderApprovedEvent;
import com.food.order.sysyem.event.publisher.DomainEventPublisher;

public interface OrderApprovedMessagePublisher extends DomainEventPublisher<OrderApprovedEvent> {
}

