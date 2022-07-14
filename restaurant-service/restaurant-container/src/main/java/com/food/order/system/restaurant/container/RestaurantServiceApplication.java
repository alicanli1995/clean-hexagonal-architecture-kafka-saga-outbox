package com.food.order.system.restaurant.container;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = { "com.food.order.system.data.access.restaurant",
        "com.food.order.system.common.data.access" })
@EntityScan(basePackages = { "com.food.order.system.data.access.restaurant",
        "com.food.order.system.common.data.access" })
@SpringBootApplication(scanBasePackages = "com.food.order")
public class RestaurantServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestaurantServiceApplication.class, args);
    }
}
