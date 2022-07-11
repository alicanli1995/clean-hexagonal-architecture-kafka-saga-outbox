package com.food.order.system.data.access.customer.mapper;

import com.food.order.domain.valueobject.CustomerId;
import com.food.order.system.data.access.customer.entity.CustomerEntity;
import com.food.order.system.domain.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataAccessMapper {

    public Customer customerEntityToCustomer(CustomerEntity customerEntity) {
        return new Customer(new CustomerId(customerEntity.getId()));
    }
}
