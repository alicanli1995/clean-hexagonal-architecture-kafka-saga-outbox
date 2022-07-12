package com.food.order.sysyem.event.publisher;

import com.food.order.sysyem.event.DomainEvent;

public interface DomainEventPublisher <T extends DomainEvent> {
    void publish(T event);
}
