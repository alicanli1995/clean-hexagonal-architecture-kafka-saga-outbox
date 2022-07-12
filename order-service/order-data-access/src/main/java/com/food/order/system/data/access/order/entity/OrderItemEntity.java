package com.food.order.system.data.access.order.entity;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;
@Entity
@DynamicUpdate
@Table(name = "order_items")
@IdClass(OrderItemEntityId.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemEntity {

    @Id
    private Long id;
    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity orderEntity;

    private UUID productId;

    private Integer quantity;

    private BigDecimal price;

    private BigDecimal subTotal;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItemEntity that)) return false;
        if (!id.equals(that.id)) return false;
        return orderEntity.equals(that.orderEntity);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + orderEntity.hashCode();
        return result;
    }
}