package com.food.order.system.payment.application.service.ports.output.repository;

import com.food.order.system.payment.service.domain.entity.Payment;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {
    Payment save(Payment payment);
    Optional<Payment> findByOrderId(UUID orderId);


}
