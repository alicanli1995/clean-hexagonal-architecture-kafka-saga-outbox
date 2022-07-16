package com.food.order.system.restaurant.domain.service.mapper;

import com.food.order.system.restaurant.domain.core.entity.OrderDetail;
import com.food.order.system.restaurant.domain.core.entity.Product;
import com.food.order.system.restaurant.domain.core.entity.Restaurant;
import com.food.order.system.valueobject.Money;
import com.food.order.system.valueobject.OrderId;
import com.food.order.system.valueobject.OrderStatus;
import com.food.order.system.valueobject.RestaurantId;
import com.food.order.system.restaurant.domain.service.dto.RestaurantApprovalRequest;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RestaurantDataMapper {

    public Restaurant restaurantApprovalRequestToRestaurant(RestaurantApprovalRequest request) {
        return Restaurant.builder()
                .restaurantId(new RestaurantId(UUID.fromString(request.getRestaurantId())))
                .orderDetail(OrderDetail.builder()
                        .orderId(new OrderId(UUID.fromString(request.getOrderId())))
                        .products(request.getProducts().stream().map(
                                product -> Product.builder()
                                        .productId(product.getId())
                                        .quantity(product.getQuantity())
                                        .build()
                        ).toList())
                        .totalAmount(new Money(request.getPrice()))
                        .status(OrderStatus.valueOf(request.getStatus().name()))
                        .build())
                .build();
    }
}
