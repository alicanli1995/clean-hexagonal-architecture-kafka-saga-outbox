package com.food.order.domain.valueobject;

import java.util.UUID;

public class OrderId extends BaseId<UUID> {

    public OrderId(UUID value) {
        super(value);
    }

}
