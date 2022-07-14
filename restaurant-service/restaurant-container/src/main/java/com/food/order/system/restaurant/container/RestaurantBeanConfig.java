package com.food.order.system.restaurant.container;

import com.food.order.system.restaurant.domain.core.RestaurantDomainService;
import com.food.order.system.restaurant.domain.core.RestaurantDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestaurantBeanConfig {

    @Bean
    public RestaurantDomainService restaurantService() {
        return new RestaurantDomainServiceImpl();
    }
}
