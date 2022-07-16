package com.food.order.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {"com.food.order.system.data.access",
        "com.food.order.system.common.data.access"})
@EntityScan(basePackages = {"com.food.order.system.data.access",
        "com.food.order.system.common.data.access"})
@SpringBootApplication(scanBasePackages = "com.food.order")
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
