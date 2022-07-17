package com.food.order.system.customer.domain;


import com.food.order.system.customer.domain.entity.Customer;
import com.food.order.system.customer.domain.event.CustomerCreatedEvent;

public interface CustomerDomainService {

    CustomerCreatedEvent validateAndInitiateCustomer(Customer customer);

}
