package com.food.order.system.ports.output.message.publisher.restaurantapproval;

import com.food.order.system.outbox.OutboxStatus;
import com.food.order.system.outbox.model.approval.OrderApprovalOutboxMessage;

import java.util.function.BiConsumer;

public interface RestaurantApprovalRequestMessagePublisher {

    void publish(OrderApprovalOutboxMessage message,
                 BiConsumer<OrderApprovalOutboxMessage, OutboxStatus> outboxCallback);

}
