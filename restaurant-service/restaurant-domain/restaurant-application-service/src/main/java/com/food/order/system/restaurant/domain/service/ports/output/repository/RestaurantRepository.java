package com.food.order.system.restaurant.domain.service.ports.output.repository;

import com.food.order.system.restaurant.domain.core.entity.Restaurant;

import java.util.Optional;

public interface RestaurantRepository {
    Optional<Restaurant> findRestaurantInformation(Restaurant restaurant);

}
