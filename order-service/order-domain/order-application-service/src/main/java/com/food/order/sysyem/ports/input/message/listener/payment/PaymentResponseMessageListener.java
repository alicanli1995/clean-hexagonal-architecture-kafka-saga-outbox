package com.food.order.sysyem.ports.input.message.listener.payment;

import com.food.order.sysyem.dto.message.PaymentResponse;

public interface PaymentResponseMessageListener {

    void paymentCompleted(PaymentResponse paymentResponse);
    void paymentCancelled(PaymentResponse paymentResponse);
}
