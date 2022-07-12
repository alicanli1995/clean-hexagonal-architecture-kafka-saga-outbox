package com.food.order.sysyem.ports.output.repository;

import com.food.order.system.domain.entity.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {
    Optional<Customer> findCustomer(UUID customerId);
}
