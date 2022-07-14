package com.food.order.system.restaurant.domain.service.ports.output.repository;

import com.food.order.system.restaurant.domain.core.entity.OrderApproval;

public interface OrderApprovalRepository {
    OrderApproval save(OrderApproval orderApproval);
}
