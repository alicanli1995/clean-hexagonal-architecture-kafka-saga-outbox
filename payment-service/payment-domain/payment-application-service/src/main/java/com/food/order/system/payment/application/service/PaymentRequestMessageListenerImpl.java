package com.food.order.system.payment.application.service;

import com.food.order.system.payment.application.service.dto.PaymentRequest;
import com.food.order.system.payment.application.service.ports.input.message.listener.PaymentRequestMessageListener;
import com.food.order.system.payment.service.domain.event.PaymentEvent;
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
        var event = paymentRequestHelper.persistPayment(paymentRequest);
        fireEvent(event);
    }
    @Override
    public void cancelPayment(PaymentRequest paymentRequest) {
        var event = paymentRequestHelper.persistCancelPayment(paymentRequest);
        fireEvent(event);
    }
    private void fireEvent(PaymentEvent event) {

        log.info("Firing event : {}", event);
        event.fire();

    }


}
