package com.food.order.system.common.data.access.entity;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantEntityId implements Serializable {

    private UUID restaurantId;
    private UUID productId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RestaurantEntityId that)) return false;
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
