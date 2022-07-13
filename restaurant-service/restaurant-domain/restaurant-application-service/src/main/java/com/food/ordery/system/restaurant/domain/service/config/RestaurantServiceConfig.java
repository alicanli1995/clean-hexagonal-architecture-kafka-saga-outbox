package com.food.ordery.system.restaurant.domain.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "restaurant-service")
public class RestaurantServiceConfig {

    private String restaurantApprovalRequestTopicName;
    private String restaurantApprovalResponseTopicName;

}
