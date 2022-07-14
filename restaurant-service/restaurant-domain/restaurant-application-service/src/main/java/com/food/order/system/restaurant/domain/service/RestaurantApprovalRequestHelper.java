package com.food.order.system.restaurant.domain.service;

import com.food.order.system.restaurant.domain.core.RestaurantDomainService;
import com.food.order.system.restaurant.domain.core.entity.Restaurant;
import com.food.order.system.restaurant.domain.core.event.OrderApprovalEvent;
import com.food.order.system.restaurant.domain.core.exception.RestaurantNotFoundException;
import com.food.order.system.restaurant.domain.service.dto.RestaurantApprovalRequest;
import com.food.order.system.restaurant.domain.service.mapper.RestaurantDataMapper;
import com.food.order.system.restaurant.domain.service.ports.output.message.publisher.OrderApprovedMessagePublisher;
import com.food.order.system.restaurant.domain.service.ports.output.message.publisher.OrderRejectedMessagePublisher;
import com.food.order.system.restaurant.domain.service.ports.output.repository.OrderApprovalRepository;
import com.food.order.system.restaurant.domain.service.ports.output.repository.RestaurantRepository;
import com.food.order.sysyem.valueobject.OrderId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class RestaurantApprovalRequestHelper {

    private final RestaurantDomainService restaurantDomainService;
    private final RestaurantDataMapper restaurantDataMapper;
    private final RestaurantRepository restaurantRepository;
    private final OrderApprovalRepository orderApprovalRepository;
    private final OrderApprovedMessagePublisher orderApprovedMessagePublisher;
    private final OrderRejectedMessagePublisher orderRejectedMessagePublisher;

    public RestaurantApprovalRequestHelper(RestaurantDomainService restaurantDomainService,
                                           RestaurantDataMapper restaurantDataMapper,
                                           RestaurantRepository restaurantRepository,
                                           OrderApprovalRepository orderApprovalRepository,
                                           OrderApprovedMessagePublisher orderApprovedMessagePublisher,
                                           OrderRejectedMessagePublisher orderRejectedMessagePublisher) {
        this.restaurantDomainService = restaurantDomainService;
        this.restaurantDataMapper = restaurantDataMapper;
        this.restaurantRepository = restaurantRepository;
        this.orderApprovalRepository = orderApprovalRepository;
        this.orderApprovedMessagePublisher = orderApprovedMessagePublisher;
        this.orderRejectedMessagePublisher = orderRejectedMessagePublisher;
    }

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

    private Restaurant findRestaurant(RestaurantApprovalRequest restaurantApprovalRequest) {
        Restaurant restaurant = restaurantDataMapper
                .restaurantApprovalRequestToRestaurant(restaurantApprovalRequest);
        Optional<Restaurant> restaurantResult = restaurantRepository.findRestaurantInformation(restaurant);
        if (restaurantResult.isEmpty()) {
            log.error("Restaurant with id " + restaurant.getId().getValue() + " not found!");
            throw new RestaurantNotFoundException("Restaurant with id " + restaurant.getId().getValue() +
                    " not found!");
        }

        Restaurant restaurantEntity = restaurantResult.get();
        restaurant.setActive(restaurantEntity.isActive());
        restaurant.getOrderDetail().getProducts().forEach(product ->
                restaurantEntity.getOrderDetail().getProducts().forEach(p -> {
                    if (p.getId().equals(product.getId())) {
                        product.updateWithConfirmedNamePriceAndAvailablity(p.getName(), p.getPrice(), p.isAvailable());
                    }
                }));
        restaurant.getOrderDetail().setId(new OrderId(UUID.fromString(restaurantApprovalRequest.getOrderId())));

        return restaurant;
    }
}
