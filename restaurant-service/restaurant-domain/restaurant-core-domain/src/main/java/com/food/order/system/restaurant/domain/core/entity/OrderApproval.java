package com.food.order.system.restaurant.domain.core.entity;

import com.food.order.system.restaurant.domain.core.valueobject.OrderApprovalId;
import com.food.order.system.entity.BaseEntity;
import com.food.order.system.valueobject.OrderApprovalStatus;
import com.food.order.system.valueobject.OrderId;
import com.food.order.system.valueobject.RestaurantId;

public class OrderApproval extends BaseEntity<OrderApprovalId> {

    private final RestaurantId restaurantId;
    private final OrderId orderId;
    private final OrderApprovalStatus status;

    private OrderApproval(Builder builder) {
        setId(builder.orderApprovalId);
        restaurantId = builder.restaurantId;
        orderId = builder.orderId;
        status = builder.status;
    }

    public static Builder builder() {
        return new Builder();
    }

    public RestaurantId getRestaurantId() {
        return restaurantId;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public OrderApprovalStatus getStatus() {
        return status;
    }


    public static final class Builder {
        private OrderApprovalId orderApprovalId;
        private RestaurantId restaurantId;
        private OrderId orderId;
        private OrderApprovalStatus status;

        private Builder() {
        }

        public Builder orderApprovalId(OrderApprovalId val) {
            orderApprovalId = val;
            return this;
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder orderId(OrderId val) {
            orderId = val;
            return this;
        }

        public Builder status(OrderApprovalStatus val) {
            status = val;
            return this;
        }

        public OrderApproval build() {
            return new OrderApproval(this);
        }
    }
}
