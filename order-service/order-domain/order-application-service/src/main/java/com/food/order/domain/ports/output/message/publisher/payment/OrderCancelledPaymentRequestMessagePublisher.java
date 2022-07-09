package com.food.order.domain.ports.output.message.publisher.payment;

import com.food.order.domain.event.publisher.DomainEventPublisher;
import com.food.order.system.domain.event.OrderCancelledEvent;

public interface OrderCancelledPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCancelledEvent> {

}
