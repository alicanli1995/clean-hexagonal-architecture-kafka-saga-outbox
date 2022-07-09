package com.food.order.system.domain.valueobject;

import com.food.order.domain.valueobject.BaseId;

import java.util.UUID;

public class OrderItemId extends BaseId<Long> {

    public OrderItemId(Long id) {
        super(id);
    }
}
