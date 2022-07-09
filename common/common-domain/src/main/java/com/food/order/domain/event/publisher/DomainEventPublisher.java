package com.food.order.domain.event.publisher;

import com.food.order.domain.event.DomainEvent;

public interface DomainEventPublisher <T extends DomainEvent> {
    void publish(T event);
}
