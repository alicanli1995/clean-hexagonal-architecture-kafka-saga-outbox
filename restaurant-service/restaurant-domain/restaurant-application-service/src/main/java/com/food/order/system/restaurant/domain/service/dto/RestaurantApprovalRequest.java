package com.food.order.system.restaurant.domain.service.dto;

import com.food.order.system.restaurant.domain.core.entity.Product;
import com.food.order.system.valueobject.RestaurantOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class RestaurantApprovalRequest {
    private String id;
    private String sagaId;
    private String restaurantId;
    private String orderId;
    private RestaurantOrderStatus status;
    private List<Product> products;
    private BigDecimal price;
    private Instant createdAt;
}
