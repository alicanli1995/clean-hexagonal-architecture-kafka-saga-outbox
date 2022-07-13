package com.food.order.system.payment.application.service.ports.input.message.listener;

import com.food.order.system.payment.application.service.dto.PaymentRequest;

public interface PaymentRequestMessageListener {
    void completePayment(PaymentRequest paymentRequest);
    void cancelPayment(PaymentRequest paymentRequest);
}
