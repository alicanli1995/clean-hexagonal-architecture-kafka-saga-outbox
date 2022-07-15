package com.food.order.sysyem.ports.output.message.publisher.restaurantapproval;

import com.food.order.system.outbox.OutboxStatus;
import com.food.order.sysyem.outbox.model.approval.OrderApprovalOutboxMessage;

import java.util.function.BiConsumer;

public interface RestaurantApprovalRequestMessagePublisher {

    void publish(OrderApprovalOutboxMessage message,
                 BiConsumer<OrderApprovalOutboxMessage, OutboxStatus> outboxCallback);

}
