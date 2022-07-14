package com.food.order.system.restaurant.domain.service.ports.input.message.listener;

import com.food.order.system.restaurant.domain.service.dto.RestaurantApprovalRequest;

public interface RestaurantApprovalRequestMessageListener {
    void approveOrder(RestaurantApprovalRequest restaurantApprovalRequest);
}
