package com.food.order.system.data.access.order.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemEntityId implements Serializable {

    private Long id;
    private OrderEntity orderEntity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemEntityId that = (OrderItemEntityId) o;
        return id.equals(that.id) && orderEntity.equals(that.orderEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderEntity);
    }
}
