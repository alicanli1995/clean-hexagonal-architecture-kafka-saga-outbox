package com.food.order.system.restaurant.domain.service.ports.output.message.publisher;

import com.food.order.system.restaurant.domain.core.event.OrderRejectedEvent;
import com.food.order.sysyem.event.publisher.DomainEventPublisher;

public interface OrderRejectedMessagePublisher extends DomainEventPublisher<OrderRejectedEvent> {
}

