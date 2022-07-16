package com.food.order.system.payment.application.service.ports.output.repository;

import com.food.order.system.payment.service.domain.entity.CreditEntry;
import com.food.order.system.valueobject.CustomerId;

import java.util.Optional;

public interface CreditEntryRepository {
    CreditEntry save(CreditEntry creditEntry);
    Optional<CreditEntry> findByCustomerId(CustomerId orderId);
}
