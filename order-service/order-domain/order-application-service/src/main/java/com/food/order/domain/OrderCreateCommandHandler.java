package com.food.order.domain;

import com.food.order.domain.dto.create.CreateOrderCommand;
import com.food.order.domain.dto.create.CreateOrderResponse;
import com.food.order.domain.mapper.OrderDataMapper;
import com.food.order.domain.ports.output.repository.CustomerRepository;
import com.food.order.domain.ports.output.repository.OrderRepository;
import com.food.order.domain.ports.output.repository.RestaurantRepository;
import com.food.order.system.domain.entity.Order;
import com.food.order.system.domain.entity.Restaurant;
import com.food.order.system.domain.exception.OrderDomainException;
import com.food.order.system.domain.service.OrderDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderCreateCommandHandler {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderDataMapper orderDataMapper;

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        log.info("createOrder: {}", createOrderCommand);
        checkCustomer(createOrderCommand.getCustomerId());
        Restaurant restaurant = checkRestaurant(createOrderCommand);
        var order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        var createdEventOrder = orderDomainService.validateAndInitiateOrder(order, restaurant);
        var savedOrder = saveOrder(order);
        log.info("createOrder with id: {}", savedOrder.getId().getValue());
        return orderDataMapper.orderToCreateOrderResponse(savedOrder,"Order created successfully");
    }

    private Restaurant checkRestaurant(CreateOrderCommand createOrderCommand) {
        return restaurantRepository.findRestaurantInformation
                (orderDataMapper.createOrderCommandToRestaurant(createOrderCommand))
                .orElseThrow(() -> new OrderDomainException("Restaurant not found. " +
                        "Please check restaurant id: " + createOrderCommand.getRestaurantId()));
    }


    private void checkCustomer(UUID customerId) {
        customerRepository.findCustomer(customerId)
                .orElseThrow(() -> new OrderDomainException("Customer not found. CustomerId: " + customerId));
    }

    private Order saveOrder(Order order){
        var savedOrder =  orderRepository.save(order);
        if (Objects.isNull(savedOrder)) {
            throw new OrderDomainException("Order not saved");
        }
        return savedOrder;
    }


}
