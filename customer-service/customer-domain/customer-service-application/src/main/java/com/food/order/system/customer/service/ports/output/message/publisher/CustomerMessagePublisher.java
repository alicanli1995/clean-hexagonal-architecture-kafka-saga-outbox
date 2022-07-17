package com.food.order.system.customer.service.ports.output.message.publisher;


import com.food.order.system.customer.domain.event.CustomerCreatedEvent;

public interface CustomerMessagePublisher {

    void publish(CustomerCreatedEvent customerCreatedEvent);

}