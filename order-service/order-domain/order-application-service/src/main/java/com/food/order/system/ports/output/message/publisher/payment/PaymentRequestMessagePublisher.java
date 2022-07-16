package com.food.order.system.ports.output.message.publisher.payment;

import com.food.order.system.outbox.OutboxStatus;
import com.food.order.system.outbox.model.payment.OrderPaymentOutboxMessage;

import java.util.function.BiConsumer;

public interface PaymentRequestMessagePublisher {

    void publish(OrderPaymentOutboxMessage message,
                 BiConsumer<OrderPaymentOutboxMessage, OutboxStatus> outboxCallback);

}
