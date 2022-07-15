package com.food.order.sysyem.helper;

import com.food.order.system.domain.entity.Order;
import com.food.order.system.domain.exception.OrderNotFoundException;
import com.food.order.system.saga.SagaStatus;
import com.food.order.sysyem.ports.output.repository.OrderRepository;
import com.food.order.sysyem.valueobject.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderSagaHelper {

    private final OrderRepository orderRepository;

    public Order findOrder(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found -> Order id :" + orderId));
    }

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    public SagaStatus orderStatusToSagaStatus(OrderStatus orderStatus) {
        return switch (orderStatus) {
            case PAID -> SagaStatus.PROCESSING;
            case APPROVED -> SagaStatus.SUCCEEDED;
            case CANCELLING -> SagaStatus.COMPENSATING;
            case CANCELLED -> SagaStatus.COMPENSATED;
            default -> SagaStatus.STARTED;
        };
    }


}
