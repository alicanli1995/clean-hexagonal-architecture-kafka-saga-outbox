package com.food.order.sysyem.saga;

import com.food.order.system.domain.service.OrderDomainService;
import com.food.order.system.saga.SagaStep;
import com.food.order.sysyem.dto.message.RestaurantApprovalResponse;
import com.food.order.sysyem.helper.OrderSagaHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderApprovalSaga implements SagaStep<RestaurantApprovalResponse> {

    private final OrderDomainService orderDomainService;
    private final OrderSagaHelper orderSagaHelper;



    @Override
    @Transactional
    public void process(RestaurantApprovalResponse data) {
        log.info("Approving order with id: {}", data.getOrderId());
        var order = orderSagaHelper.findOrder(data.getOrderId());
        orderDomainService.approve(order);
        orderSagaHelper.saveOrder(order);
        log.info("Order approved: {}", order);
    }


    @Override
    @Transactional
    public void rollback(RestaurantApprovalResponse data) {
        log.info("Approving order with id: {}", data.getOrderId());
        var order = orderSagaHelper.findOrder(data.getOrderId());
        var cancelEvent = orderDomainService.cancelOrderPayment
                (order,
                data.getFailureMessages());
        orderSagaHelper.saveOrder(order);
        log.info("Order cancelled: {}", order);
    }
}
