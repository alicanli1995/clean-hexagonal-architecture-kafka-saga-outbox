package com.food.order.sysyem;

import com.food.order.sysyem.dto.message.PaymentResponse;
import com.food.order.sysyem.ports.input.message.listener.payment.PaymentResponseMessageListener;
import com.food.order.sysyem.saga.OrderPaymentSaga;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Slf4j
@RequiredArgsConstructor
public class PaymentResponseMessageListenerImpl implements PaymentResponseMessageListener {

    private final OrderPaymentSaga orderPaymentSaga;

    @Override
    public void paymentCompleted(PaymentResponse paymentResponse) {
        var paidEvent = orderPaymentSaga.process(paymentResponse);
        log.info("Payment completed for order with id: {}", paidEvent.getOrder().getId());
        paidEvent.fire();
    }

    @Override
    public void paymentCancelled(PaymentResponse paymentResponse) {
        orderPaymentSaga.rollback(paymentResponse);
        log.info("Payment cancelled for order with id: {}", paymentResponse.getOrderId());
    }
}
