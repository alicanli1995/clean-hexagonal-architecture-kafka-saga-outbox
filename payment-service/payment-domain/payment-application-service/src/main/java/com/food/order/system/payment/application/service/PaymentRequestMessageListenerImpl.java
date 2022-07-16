package com.food.order.system.payment.application.service;

import com.food.order.system.payment.application.service.dto.PaymentRequest;
import com.food.order.system.payment.application.service.ports.input.message.listener.PaymentRequestMessageListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentRequestMessageListenerImpl implements PaymentRequestMessageListener {

    private final PaymentRequestHelper paymentRequestHelper;

    @Override
    public void completePayment(PaymentRequest paymentRequest) {
        paymentRequestHelper.persistPayment(paymentRequest);
    }
    @Override
    public void cancelPayment(PaymentRequest paymentRequest) {
       paymentRequestHelper.persistCancelPayment(paymentRequest);
    }



}
