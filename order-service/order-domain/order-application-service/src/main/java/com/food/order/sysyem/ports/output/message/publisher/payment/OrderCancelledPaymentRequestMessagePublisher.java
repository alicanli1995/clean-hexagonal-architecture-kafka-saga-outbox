package com.food.order.sysyem.ports.output.message.publisher.payment;

import com.food.order.sysyem.event.publisher.DomainEventPublisher;
import com.food.order.system.domain.event.OrderCancelledEvent;

public interface OrderCancelledPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCancelledEvent> {

}