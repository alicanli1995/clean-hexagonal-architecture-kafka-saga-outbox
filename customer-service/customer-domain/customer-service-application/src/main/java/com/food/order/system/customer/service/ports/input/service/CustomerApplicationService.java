package com.food.order.system.customer.service.ports.input.service;


import com.food.order.system.customer.service.create.CreateCustomerCommand;
import com.food.order.system.customer.service.create.CreateCustomerResponse;

import javax.validation.Valid;

public interface CustomerApplicationService {

    CreateCustomerResponse createCustomer(@Valid CreateCustomerCommand createCustomerCommand);

}
