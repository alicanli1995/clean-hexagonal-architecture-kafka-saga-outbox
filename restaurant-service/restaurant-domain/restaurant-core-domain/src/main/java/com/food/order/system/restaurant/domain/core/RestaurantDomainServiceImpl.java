package com.food.order.system.restaurant.domain.core;

import com.food.order.system.restaurant.domain.core.entity.Restaurant;
import com.food.order.system.restaurant.domain.core.event.OrderApprovalEvent;
import com.food.order.system.restaurant.domain.core.event.OrderApprovedEvent;
import com.food.order.system.restaurant.domain.core.event.OrderRejectedEvent;
import com.food.order.system.event.publisher.DomainEventPublisher;
import com.food.order.system.valueobject.OrderApprovalStatus;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static com.food.order.system.DomainConstants.UTC;

@Slf4j
public class RestaurantDomainServiceImpl implements RestaurantDomainService {

    @Override
    public OrderApprovalEvent validateOrder(Restaurant restaurant,
                                            List<String> failureMessages,
                                            DomainEventPublisher<OrderApprovedEvent> publisher,
                                            DomainEventPublisher<OrderRejectedEvent> rejectedPublisher) {
        restaurant.validateOrder(failureMessages);
        log.info("Order validation with id {}", restaurant.getOrderDetail().getId());
        if (failureMessages.isEmpty()) {
            log.info("Order validation with id {} is successful", restaurant.getOrderDetail().getId());
            restaurant.constructOrderApproval(OrderApprovalStatus.APPROVED);
            return new OrderApprovedEvent(restaurant.getOrderApproval(), restaurant.getId(),
                    failureMessages, ZonedDateTime.now(ZoneId.of(UTC)), publisher);
        } else {
            log.info("Order validation with id {} is failed", restaurant.getOrderDetail().getId());
            restaurant.constructOrderApproval(OrderApprovalStatus.REJECTED);
            return new OrderRejectedEvent(restaurant.getOrderApproval(), restaurant.getId(),
                    failureMessages, ZonedDateTime.now(), rejectedPublisher);
        }

    }
}
