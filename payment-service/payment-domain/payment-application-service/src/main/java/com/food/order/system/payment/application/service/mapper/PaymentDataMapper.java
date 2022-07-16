package com.food.order.system.payment.application.service.mapper;

import com.food.order.system.payment.application.service.dto.PaymentRequest;
import com.food.order.system.payment.application.service.outbox.model.OrderEventPayload;
import com.food.order.system.payment.service.domain.entity.Payment;
import com.food.order.system.payment.service.domain.event.PaymentEvent;
import com.food.order.system.valueobject.CustomerId;
import com.food.order.system.valueobject.Money;
import com.food.order.system.valueobject.OrderId;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PaymentDataMapper {


    public Payment paymentRequestModelToPayment(PaymentRequest paymentRequest) {
        return Payment.builder()
                .customerId(new CustomerId(UUID.fromString(paymentRequest.getCustomerId())))
                .orderId(new OrderId(UUID.fromString(paymentRequest.getOrderId())))
                .price(new Money(paymentRequest.getPrice()))
                .build();
    }

    public OrderEventPayload paymentEventToOrderEventPayload(PaymentEvent payment) {
        return OrderEventPayload.builder()
                .orderId(payment.getPayment().getOrderId().getValue().toString())
                .customerId(payment.getPayment().getCustomerId().getValue().toString())
                .price(payment.getPayment().getPrice().getAmount())
                .paymentId(payment.getPayment().getId().toString())
                .createdAt(payment.getCreatedAt())
                .failureMessages(payment.getFailureMessages())
                .paymentStatus(payment.getPayment().getStatus().toString())
                .build();

    }
}
