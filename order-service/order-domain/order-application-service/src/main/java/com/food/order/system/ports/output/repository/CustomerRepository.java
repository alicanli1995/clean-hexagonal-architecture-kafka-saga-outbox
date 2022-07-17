package com.food.order.system.ports.output.repository;

import com.food.order.system.domain.entity.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {

    Customer save(Customer customer);
    Optional<Customer> findCustomer(UUID customerId);
}
