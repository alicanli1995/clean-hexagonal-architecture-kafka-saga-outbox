package com.food.order.system.restaurant.domain.core.valueobject;

import com.food.order.system.valueobject.BaseId;

import java.util.UUID;

public class OrderApprovalId extends BaseId<UUID> {
    public OrderApprovalId(UUID value) {
        super(value);
    }
}
