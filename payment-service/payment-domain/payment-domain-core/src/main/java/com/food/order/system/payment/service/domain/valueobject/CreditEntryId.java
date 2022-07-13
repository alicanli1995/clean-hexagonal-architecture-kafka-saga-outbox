package com.food.order.system.payment.service.domain.valueobject;

import com.food.order.sysyem.valueobject.BaseId;

import java.util.UUID;

public class CreditEntryId extends BaseId<UUID> {
    public CreditEntryId(UUID value) {
        super(value);
    }
}
