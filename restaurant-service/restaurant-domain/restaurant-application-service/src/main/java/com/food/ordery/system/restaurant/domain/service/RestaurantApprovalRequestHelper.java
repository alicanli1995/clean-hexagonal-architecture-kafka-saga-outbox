package com.food.ordery.system.restaurant.domain.service;

import com.food.order.system.restaurant.domain.core.RestaurantDomainService;
import com.food.order.system.restaurant.domain.core.entity.Restaurant;
import com.food.order.system.restaurant.domain.core.event.OrderApprovalEvent;
import com.food.order.system.restaurant.domain.core.exception.RestaurantNotFoundException;
import com.food.order.sysyem.valueobject.OrderId;
import com.food.ordery.system.restaurant.domain.service.dto.RestaurantApprovalRequest;
import com.food.ordery.system.restaurant.domain.service.mapper.RestaurantDataMapper;
import com.food.ordery.system.restaurant.domain.service.ports.output.message.publisher.OrderApprovedMessagePublisher;
import com.food.ordery.system.restaurant.domain.service.ports.output.message.publisher.OrderRejectedMessagePublisher;
import com.food.ordery.system.restaurant.domain.service.ports.output.repository.OrderApprovalRepository;
import com.food.ordery.system.restaurant.domain.service.ports.output.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestaurantApprovalRequestHelper {

    private final RestaurantDomainService restaurantDomainService;
    private final RestaurantDataMapper restaurantDataMapper;
    private final RestaurantRepository restaurantRepository;
    private final OrderApprovalRepository orderApprovalRepository;
    private final OrderApprovedMessagePublisher orderApprovedMessagePublisher;
    private final OrderRejectedMessagePublisher orderRejectedMessagePublisher;

    @Transactional
    public OrderApprovalEvent persistOrderApproval(RestaurantApprovalRequest request) {
        log.info("Persisting order approval request: {}", request);
        List<String> failureMessages = new ArrayList<>();
        var restaurant = findRestaurant(request);
        var event = restaurantDomainService.validateOrder
                (restaurant, failureMessages, orderApprovedMessagePublisher, orderRejectedMessagePublisher);

        orderApprovalRepository.save(restaurant.getOrderApproval());
        return event;

    }

    private Restaurant findRestaurant(RestaurantApprovalRequest request) {
        var restaurant = restaurantDataMapper.restaurantApprovalRequestToRestaurant(request);
        var resultRestaurant =  restaurantRepository.findRestaurantInformation(restaurant).orElseThrow(
                () -> new RestaurantNotFoundException("Restaurant not found")
        );
        restaurant.setActive(resultRestaurant.isActive());
        restaurant.getOrderDetail().getProducts().forEach(product -> {
            resultRestaurant.getOrderDetail().getProducts().forEach(p -> {
                if (p.getId().equals(product.getId())) {
                    p.updateWithConfirmedNamePriceAndAvailablity(p.getName(),p.getPrice(), p.isAvailable());
                }});
        });
        restaurant.getOrderDetail().setId(new OrderId(UUID.fromString(request.getOrderId())));
        return restaurant;
    }
}
