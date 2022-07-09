package com.food.order.domain.ports.output.message.publisher.restaurantapproval;

import com.food.order.domain.event.publisher.DomainEventPublisher;
import com.food.order.system.domain.event.OrderPaidEvent;

public interface OrderPaidRestaurantRequestMessagePublisher extends DomainEventPublisher<OrderPaidEvent> {

}
