package com.food.order.system.order.domain;

import com.food.order.system.domain.service.OrderDomainService;
import com.food.order.system.domain.service.impl.OrderDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public OrderDomainService orderDomainService() {
        return new OrderDomainServiceImpl();
    }
}
