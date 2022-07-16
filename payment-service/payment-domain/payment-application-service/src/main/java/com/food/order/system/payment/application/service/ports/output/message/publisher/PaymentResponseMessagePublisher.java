package com.food.order.system.payment.application.service.ports.output.message.publisher;

import com.food.order.system.outbox.OutboxStatus;
import com.food.order.system.payment.application.service.outbox.model.OrderOutboxMessage;

import java.util.function.BiConsumer;

public interface PaymentResponseMessagePublisher {
    void publish(OrderOutboxMessage message,
                 BiConsumer<OrderOutboxMessage , OutboxStatus> updateOutboxMessage);
}
