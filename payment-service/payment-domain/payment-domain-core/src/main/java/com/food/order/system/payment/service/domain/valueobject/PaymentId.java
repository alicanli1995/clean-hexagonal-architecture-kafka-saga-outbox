package com.food.order.system.payment.service.domain.valueobject;

import com.food.order.system.valueobject.BaseId;

import java.util.UUID;

public class PaymentId extends BaseId<UUID> {
    public PaymentId(UUID id) {
        super(id);
    }
}
