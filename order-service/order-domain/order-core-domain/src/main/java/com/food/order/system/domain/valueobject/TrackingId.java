package com.food.order.system.domain.valueobject;

import com.food.order.sysyem.valueobject.BaseId;

import java.util.UUID;

public class TrackingId extends BaseId<UUID> {
    public TrackingId(UUID id) {
        super(id);
    }
}
