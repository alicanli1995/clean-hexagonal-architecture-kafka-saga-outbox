package com.food.order.sysyem.ports.output.message.publisher.restaurantapproval;

import com.food.order.sysyem.event.publisher.DomainEventPublisher;
import com.food.order.system.domain.event.OrderPaidEvent;

public interface OrderPaidRestaurantRequestMessagePublisher extends DomainEventPublisher<OrderPaidEvent> {

}
