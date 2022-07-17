package com.food.order.system.customer.domain.exception;


import com.food.order.system.exception.DomainException;

public class CustomerDomainException extends DomainException {

    public CustomerDomainException(String message) {
        super(message);
    }
}
