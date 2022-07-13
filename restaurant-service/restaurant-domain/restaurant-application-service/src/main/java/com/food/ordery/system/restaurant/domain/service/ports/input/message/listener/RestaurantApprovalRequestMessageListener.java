package com.food.ordery.system.restaurant.domain.service.ports.input.message.listener;

import com.food.ordery.system.restaurant.domain.service.dto.RestaurantApprovalRequest;

public interface RestaurantApprovalRequestMessageListener {
    void approveOrder(RestaurantApprovalRequest request);
}
