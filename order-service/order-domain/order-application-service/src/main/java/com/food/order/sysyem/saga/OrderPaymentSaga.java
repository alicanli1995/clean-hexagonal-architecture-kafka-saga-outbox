package com.food.order.sysyem.saga;

import com.food.order.system.domain.event.OrderPaidEvent;
import com.food.order.system.domain.service.OrderDomainService;
import com.food.order.system.saga.SagaStep;
import com.food.order.sysyem.dto.message.PaymentResponse;
import com.food.order.sysyem.event.EmptyEvent;
import com.food.order.sysyem.helper.OrderSagaHelper;
import com.food.order.sysyem.ports.output.message.publisher.restaurantapproval.OrderPaidRestaurantRequestMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPaymentSaga implements SagaStep<PaymentResponse, OrderPaidEvent, EmptyEvent> {

    private final OrderDomainService orderDomainService;
    private final OrderSagaHelper orderSagaHelper;
    private final OrderPaidRestaurantRequestMessagePublisher orderPaidRestaurantRequestMessagePublisher;

    @Override
    @Transactional
    public OrderPaidEvent process(PaymentResponse data) {
        log.info("Completing payment for order with id: {}", data.getOrderId());
        var order = orderSagaHelper.findOrder(data.getOrderId());
        var paidEvent = orderDomainService.payOrder(order,orderPaidRestaurantRequestMessagePublisher);
        orderSagaHelper.saveOrder(order);
        log.info("Payment completed for order with id: {}", order.getId());
        return paidEvent;
    }

    @Override
    @Transactional
    public EmptyEvent rollback(PaymentResponse data) {
        log.info("Rolling back payment for order with id: {}", data.getOrderId());
        var order = orderSagaHelper.findOrder(data.getOrderId());
        orderDomainService.cancelOrder(order,data.getFailureMessages());
        orderSagaHelper.saveOrder(order);
        log.info("Payment rolled back for order with id: {}", order.getId());
        return EmptyEvent.INSTANCE;
    }
}
