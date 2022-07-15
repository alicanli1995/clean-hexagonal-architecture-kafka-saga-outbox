package com.food.order.sysyem.outbox.model.payment;

import com.food.order.system.outbox.OutboxStatus;
import com.food.order.system.saga.SagaStatus;
import com.food.order.sysyem.valueobject.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class OrderPaymentOutboxMessage {
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