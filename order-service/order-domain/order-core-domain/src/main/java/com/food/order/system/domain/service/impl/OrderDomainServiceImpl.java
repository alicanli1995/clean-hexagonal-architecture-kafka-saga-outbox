package com.food.order.system.domain.service.impl;

import com.food.order.system.domain.entity.Order;
import com.food.order.system.domain.entity.Restaurant;
import com.food.order.system.domain.event.OrderCancelledEvent;
import com.food.order.system.domain.event.OrderCreatedEvent;
import com.food.order.system.domain.event.OrderPaidEvent;
import com.food.order.system.domain.exception.OrderDomainException;
import com.food.order.system.domain.service.OrderDomainService;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService {

    private static final String UTC = "UTC";

    @Override
    public OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant) {
         validateRestaurant(restaurant);
         setOrderProductInformation(order,restaurant);
         order.validateOrder();
         order.initializeOrder();
         log.info("Order with id {} initialize successfully", order.getId().getValue());
         return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    private void setOrderProductInformation(Order order, Restaurant restaurant) {
        order.getItems()
                .forEach(orderItem -> restaurant.getProducts().forEach(restaurantProduct -> {
                    var currentProduct = orderItem.getProduct();
                    if(currentProduct.equals(restaurantProduct)){
                        currentProduct.updateWithConfirmedNameAndPrice(restaurantProduct.getName(),
                                restaurantProduct.getPrice());
                    }
                }));
    }

    private void validateRestaurant(Restaurant restaurant) {
        if (Boolean.FALSE.equals(restaurant.isActive())) {
            throw new OrderDomainException("Restaurant is not active, please try again later. " +
                    "Restaurant id: " + restaurant.getId());
        }
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        order.pay();
        log.info("Order with id {} paid successfully", order.getId().getValue());
        return new OrderPaidEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void approve(Order order) {
        order.approve();
        log.info("Order with id {} approved successfully", order.getId().getValue());
    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
        order.initCancel(failureMessages);
        log.info("Order with id {} cancelled successfully", order.getId().getValue());
        return new OrderCancelledEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {
        order.cancel(failureMessages);
        log.info("Order with id {} cancelled successfully", order.getId().getValue());
    }
}
