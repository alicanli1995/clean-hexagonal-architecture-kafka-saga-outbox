package com.food.order.system.restaurant.domain.service;

import com.food.order.system.restaurant.domain.service.dto.RestaurantApprovalRequest;
import com.food.order.system.restaurant.domain.service.ports.input.message.listener.RestaurantApprovalRequestMessageListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantApprovalRequestMessageListenerImpl implements RestaurantApprovalRequestMessageListener {
    private final RestaurantApprovalRequestHelper restaurantApprovalRequestHelper;

    @Override
    public void approveOrder(RestaurantApprovalRequest request) {
       restaurantApprovalRequestHelper.persistOrderApproval(request);
    }
}
