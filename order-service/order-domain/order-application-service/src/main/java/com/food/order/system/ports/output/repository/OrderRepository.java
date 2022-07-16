package com.food.order.system.ports.output.repository;

import com.food.order.system.domain.entity.Order;
import com.food.order.system.domain.valueobject.TrackingId;
import com.food.order.system.valueobject.OrderId;

import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(OrderId trackingId);

    Optional<Order> findByTrackingId(TrackingId trackingId);


}
