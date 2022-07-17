package com.food.order.system.customer.domain.entity;


import com.food.order.system.entity.AggregateRoot;
import com.food.order.system.valueobject.CustomerId;

public class Customer extends AggregateRoot<CustomerId> {
    private final String username;
    private final String firstName;
    private final String lastName;

    public Customer(CustomerId customerId, String username, String firstName, String lastName) {
        super.setId(customerId);
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}


