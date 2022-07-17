package com.food.order.system.customer.service.ports.output.repository;


import com.food.order.system.customer.domain.entity.Customer;

public interface CustomerRepository {

    Customer createCustomer(Customer customer);
}
