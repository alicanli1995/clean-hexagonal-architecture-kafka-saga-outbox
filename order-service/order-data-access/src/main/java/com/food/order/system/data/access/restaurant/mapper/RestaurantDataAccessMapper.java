package com.food.order.system.data.access.restaurant.mapper;

import com.food.order.system.common.data.access.entity.RestaurantEntity;
import com.food.order.system.common.data.access.exception.RestaurantDataAccessException;
import com.food.order.system.domain.entity.Product;
import com.food.order.system.domain.entity.Restaurant;
import com.food.order.system.valueobject.Money;
import com.food.order.system.valueobject.ProductId;
import com.food.order.system.valueobject.RestaurantId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class RestaurantDataAccessMapper {

    public List<UUID> restaurantToRestaurantProducts(Restaurant restaurant) {
        return restaurant.getProducts()
                .stream()
                .map(product -> product.getId().getValue())
                .toList();
    }

    public Restaurant restaurantEntityToRestaurant(List<RestaurantEntity> restaurantEntities) {
        var restaurantEntity =
                restaurantEntities.stream().findFirst()
                        .orElseThrow(() -> new RestaurantDataAccessException("No restaurant found"));
        var restaurantProducts = restaurantEntities.stream()
                .map(entity -> new Product(new ProductId(entity.getProductId()),
                        entity.getProductName(),
                        new Money(entity.getProductPrice()))).toList();
        return Restaurant.builder()
                .id(new RestaurantId(restaurantEntity.getRestaurantId()))
                .products(restaurantProducts)
                .isActive(restaurantEntity.getRestaurantActive())
                .build();

    }
}
