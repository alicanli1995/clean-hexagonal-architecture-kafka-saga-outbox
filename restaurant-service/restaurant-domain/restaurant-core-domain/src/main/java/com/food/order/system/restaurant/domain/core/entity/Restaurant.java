package com.food.order.system.restaurant.domain.core.entity;

import com.food.order.system.restaurant.domain.core.valueobject.OrderApprovalId;
import com.food.order.system.entity.AggregateRoot;
import com.food.order.system.valueobject.Money;
import com.food.order.system.valueobject.OrderApprovalStatus;
import com.food.order.system.valueobject.OrderStatus;
import com.food.order.system.valueobject.RestaurantId;

import java.util.List;
import java.util.UUID;

public class Restaurant extends AggregateRoot<RestaurantId> {
    private OrderApproval orderApproval;
    private boolean active;
    private final OrderDetail orderDetail;

    public void validateOrder(List<String> failureMessages){
        if (!orderDetail.getStatus().equals(OrderStatus.PAID)){
            failureMessages.add("Order is not paid");
        }
        var totalAmount = orderDetail.getProducts()
                .stream()
                .map(product -> {
                    if (Boolean.FALSE.equals(product.isAvailable()))
                    {
                        failureMessages.add("Product is not available");
                    }
                    return product.getPrice().multiply(product.getQuantity());
                })
                .reduce(Money.ZERO, Money::add);

        if(!totalAmount.equals(orderDetail.getTotalAmount()))
        {
            failureMessages.add("Total amount is not correct");
        }

    }

    public void constructOrderApproval(OrderApprovalStatus status){
        this.orderApproval = OrderApproval.builder()
                .orderApprovalId(new OrderApprovalId(UUID.randomUUID()))
                .restaurantId(this.getId())
                .orderId(this.getOrderDetail().getId())
                .status(status)
                .build();
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    private Restaurant(Builder builder) {
        setId(builder.restaurantId);
        orderApproval = builder.orderApproval;
        active = builder.active;
        orderDetail = builder.orderDetail;
    }

    public static Builder builder() {
        return new Builder();
    }

    public OrderApproval getOrderApproval() {
        return orderApproval;
    }

    public boolean isActive() {
        return active;
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }


    public static final class Builder {
        private RestaurantId restaurantId;
        private OrderApproval orderApproval;
        private boolean active;
        private OrderDetail orderDetail;

        private Builder() {
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder orderApproval(OrderApproval val) {
            orderApproval = val;
            return this;
        }

        public Builder active(boolean val) {
            active = val;
            return this;
        }

        public Builder orderDetail(OrderDetail val) {
            orderDetail = val;
            return this;
        }

        public Restaurant build() {
            return new Restaurant(this);
        }
    }
}
