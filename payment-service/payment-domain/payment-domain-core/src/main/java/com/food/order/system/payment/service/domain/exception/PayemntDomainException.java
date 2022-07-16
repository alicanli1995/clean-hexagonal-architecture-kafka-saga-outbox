package com.food.order.system.payment.service.domain.exception;

import com.food.order.system.exception.DomainException;

public class PayemntDomainException extends DomainException {
    public PayemntDomainException(String message) {
        super(message);
    }

    public PayemntDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
