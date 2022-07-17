package com.food.order.system.ports.input.message.listener.customer;

import com.food.order.system.dto.message.CustomerModel;

public interface CustomerMessageListener {
    void customerCreated(CustomerModel customerModel);
}
