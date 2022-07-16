package com.food.order.system.payment.application.service.ports.output.repository;

import com.food.order.system.payment.service.domain.entity.CreditHistory;
import com.food.order.system.valueobject.CustomerId;

import java.util.List;
import java.util.Optional;

public interface CreditHistoryRepository {
    CreditHistory save(CreditHistory creditHistory);
    Optional<List<CreditHistory>> findByCustomerId(CustomerId orderId);
}
