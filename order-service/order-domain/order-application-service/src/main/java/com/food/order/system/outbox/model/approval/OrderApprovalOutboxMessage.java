package com.food.order.system.outbox.model.approval;

import com.food.order.system.outbox.OutboxStatus;
import com.food.order.system.saga.SagaStatus;
import com.food.order.system.valueobject.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class OrderApprovalOutboxMessage {

    private UUID id;

    private UUID sagaId;

    private ZonedDateTime createdAt;

    @Setter
    private ZonedDateTime processedAt;

    private String type;

    private String payload;

    @Setter
    private SagaStatus sagaStatus;

    @Setter
    private OrderStatus orderStatus;

    @Setter
    private OutboxStatus outboxStatus;

    private int version;
}
