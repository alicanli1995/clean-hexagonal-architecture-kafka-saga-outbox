package com.food.order.system.payment.application.service.outbox.model;

import com.food.order.system.outbox.OutboxStatus;
import com.food.order.system.valueobject.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class OrderOutboxMessage {

    private UUID id;

    private UUID sagaId;

    private ZonedDateTime createdAt;

    private ZonedDateTime processedAt;

    private String type;

    private String payload;

    private PaymentStatus paymentStatus;

    @Setter
    private OutboxStatus outboxStatus;

    private int version;


}
