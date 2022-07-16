package com.food.order.system.event.publisher;

import com.food.order.system.event.DomainEvent;

public interface DomainEventPublisher <T extends DomainEvent> {
    void publish(T event);
}
