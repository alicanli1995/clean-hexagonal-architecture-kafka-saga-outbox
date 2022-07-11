package com.food.order.system.data.access.restaurant.adapter;

import com.food.order.domain.ports.output.repository.RestaurantRepository;
import com.food.order.system.data.access.restaurant.mapper.RestaurantDataAccessMapper;
import com.food.order.system.data.access.restaurant.repository.RestaurantJpaRepository;
import com.food.order.system.domain.entity.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final RestaurantJpaRepository restaurantJpaRepository;
    private final RestaurantDataAccessMapper restaurantDataAccessMapper;

    @Override
    public Optional<Restaurant> findRestaurantInformation(Restaurant restaurant) {
        return restaurantJpaRepository.findByRestaurantIdAndProductIdIn
                        (restaurant.getId().getValue(),
                                restaurantDataAccessMapper.restaurantToRestaurantProducts(restaurant))
                .map(restaurantDataAccessMapper::restaurantEntityToRestaurant);
    }
}
