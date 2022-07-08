package com.food.order.domain.valueobject;

import java.util.UUID;

public class RestaurantId extends BaseId<UUID> {

    public RestaurantId(UUID value) {
        super(value);
    }

}
