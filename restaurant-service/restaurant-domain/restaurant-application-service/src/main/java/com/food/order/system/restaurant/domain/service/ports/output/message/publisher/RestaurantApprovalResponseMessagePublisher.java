package com.food.order.system.restaurant.domain.service.ports.output.message.publisher;

import com.food.order.system.outbox.OutboxStatus;
import com.food.order.system.restaurant.domain.service.outbox.model.OrderOutboxMessage;

import java.util.function.BiConsumer;

public interface RestaurantApprovalResponseMessagePublisher {

    void publish(OrderOutboxMessage orderOutboxMessage,
                 BiConsumer<OrderOutboxMessage, OutboxStatus> outboxCallback);
}
