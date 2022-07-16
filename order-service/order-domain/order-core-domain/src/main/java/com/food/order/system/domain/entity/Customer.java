package com.food.order.system.domain.entity;

import com.food.order.system.entity.AggregateRoot;
import com.food.order.system.valueobject.CustomerId;

public class Customer extends AggregateRoot<CustomerId> {

    public Customer(){

    }
    public Customer (CustomerId id) {
        super.setId(id);
    }
}
