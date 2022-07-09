package com.food.order.domain.ports.output.message.publisher.payment;

import com.food.order.domain.event.publisher.DomainEventPublisher;
import com.food.order.system.domain.event.OrderCreatedEvent;

public interface OrderCreatedPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCreatedEvent> {

}
