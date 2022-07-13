package com.food.order.system.payment.service.domain.event;

import com.food.order.system.payment.service.domain.entity.Payment;
import com.food.order.sysyem.event.publisher.DomainEventPublisher;

import java.time.ZonedDateTime;
import java.util.List;

public class PaymentFailedEvent extends PaymentEvent{

    private final DomainEventPublisher<PaymentFailedEvent> publisher;

    public PaymentFailedEvent(Payment payment, ZonedDateTime createdAt, List<String> failureMessages,
                              DomainEventPublisher<PaymentFailedEvent> publisher) {
        super(payment, createdAt, failureMessages);
        this.publisher = publisher;
    }

    @Override
    public void fire() {
        publisher.publish(this);
    }
}
