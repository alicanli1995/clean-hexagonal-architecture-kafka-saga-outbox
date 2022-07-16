package com.food.order.system.payment.service.domain.valueobject;

import com.food.order.system.valueobject.BaseId;

import java.util.UUID;

public class CreditHistoryId extends BaseId<UUID> {

    public CreditHistoryId(UUID value) {
        super(value);
    }
}
