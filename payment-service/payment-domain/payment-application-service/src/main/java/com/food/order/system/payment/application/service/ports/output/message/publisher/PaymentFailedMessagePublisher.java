package com.food.order.system.payment.application.service.ports.output.message.publisher;

import com.food.order.system.payment.service.domain.event.PaymentFailedEvent;
import com.food.order.system.event.publisher.DomainEventPublisher;

public interface PaymentFailedMessagePublisher extends DomainEventPublisher<PaymentFailedEvent> {
}
