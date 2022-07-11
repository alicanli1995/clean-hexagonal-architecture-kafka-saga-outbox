package com.food.order.system.data.access.restaurant.entity;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_restaurant_m_view",schema = "restaurant")
@Getter
@Setter
@Builder
@IdClass(RestaurantEntityId.class)
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantEntity {

    @Id
    private UUID restaurantId;

    @Id
    private UUID productId;

    private String restaurantName;

    private Boolean restaurantActive;

    private String productName;

    private BigDecimal productPrice;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RestaurantEntity that)) return false;
        if (!restaurantId.equals(that.restaurantId)) return false;
        return productId.equals(that.productId);
    }

    @Override
    public int hashCode() {
        int result = restaurantId.hashCode();
        result = 31 * result + productId.hashCode();
        return result;
    }
}
