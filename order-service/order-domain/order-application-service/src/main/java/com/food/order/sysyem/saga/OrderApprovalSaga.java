package com.food.order.sysyem.saga;

import com.food.order.system.domain.event.OrderCancelledEvent;
import com.food.order.system.domain.service.OrderDomainService;
import com.food.order.system.saga.SagaStep;
import com.food.order.sysyem.dto.message.RestaurantApprovalResponse;
import com.food.order.sysyem.event.EmptyEvent;
import com.food.order.sysyem.helper.OrderSagaHelper;
import com.food.order.sysyem.ports.output.message.publisher.payment.OrderCancelledPaymentRequestMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderApprovalSaga implements SagaStep<RestaurantApprovalResponse, EmptyEvent, OrderCancelledEvent> {

    private final OrderDomainService orderDomainService;
    private final OrderCancelledPaymentRequestMessagePublisher messagePublisher;
    private final OrderSagaHelper orderSagaHelper;

    @Override
    @Transactional
    public EmptyEvent process(RestaurantApprovalResponse data) {
        log.info("Approving order with id: {}", data.getOrderId());
        var order = orderSagaHelper.findOrder(data.getOrderId());
        orderDomainService.approve(order);
        orderSagaHelper.saveOrder(order);
        log.info("Order approved: {}", order);
        return EmptyEvent.INSTANCE;
    }


    @Override
    @Transactional
    public OrderCancelledEvent rollback(RestaurantApprovalResponse data) {
        log.info("Approving order with id: {}", data.getOrderId());
        var order = orderSagaHelper.findOrder(data.getOrderId());
        var cancelEvent = orderDomainService.cancelOrderPayment(order,data.getFailureMessages(),
                messagePublisher);
        orderSagaHelper.saveOrder(order);
        log.info("Order cancelled: {}", order);
        return cancelEvent;
    }
}
