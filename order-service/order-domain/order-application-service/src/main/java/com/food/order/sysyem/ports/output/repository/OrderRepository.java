package com.food.order.sysyem.ports.output.repository;

import com.food.order.system.domain.entity.Order;
import com.food.order.system.domain.valueobject.TrackingId;

import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findByTrackingId(TrackingId trackingId);


}
