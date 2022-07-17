package com.food.order.system.customer.service.mapper;

import com.food.order.system.customer.domain.entity.Customer;
import com.food.order.system.customer.service.create.CreateCustomerCommand;
import com.food.order.system.customer.service.create.CreateCustomerResponse;
import com.food.order.system.valueobject.CustomerId;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataMapper {

    public Customer createCustomerCommandToCustomer(CreateCustomerCommand createCustomerCommand) {
        return new Customer(new CustomerId(createCustomerCommand.customerId()),
                createCustomerCommand.username(),
                createCustomerCommand.firstName(),
                createCustomerCommand.lastName());
    }

    public CreateCustomerResponse customerToCreateCustomerResponse(Customer customer, String message) {
        return new CreateCustomerResponse(customer.getId().getValue(), message);
    }
}
