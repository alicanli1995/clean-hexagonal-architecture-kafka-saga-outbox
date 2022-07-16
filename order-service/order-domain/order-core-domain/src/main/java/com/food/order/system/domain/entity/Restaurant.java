package com.food.order.system.domain.entity;

import com.food.order.system.entity.AggregateRoot;
import com.food.order.system.valueobject.RestaurantId;

import java.util.List;

public class Restaurant extends AggregateRoot<RestaurantId> {
    private final List<Product> products;
    private final boolean isActive;

    private Restaurant(Builder builder) {
        super.setId(builder.restaurantId);
        products = builder.products;
        isActive = builder.isActive;
    }

    public static Builder builder() {
        return new Builder();
    }

    public List<Product> getProducts() {
        return products;
    }

    public boolean isActive() {
        return isActive;
    }


    public static final class Builder {
        private RestaurantId restaurantId;
        private List<Product> products;
        private boolean isActive;

        private Builder() {
        }

        public Builder id(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder products(List<Product> val) {
            products = val;
            return this;
        }

        public Builder isActive(boolean val) {
            isActive = val;
            return this;
        }

        public Restaurant build() {
            return new Restaurant(this);
        }
    }
}
