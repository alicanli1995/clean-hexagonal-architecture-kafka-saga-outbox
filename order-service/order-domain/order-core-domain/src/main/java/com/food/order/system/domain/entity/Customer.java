package com.food.order.system.domain.entity;

import com.food.order.domain.entity.AggregateRoot;
import com.food.order.domain.valueobject.CustomerId;

public class Customer extends AggregateRoot<CustomerId> {

    public Customer(){

    }
    public Customer (CustomerId id) {
        super.setId(id);
    }
}
