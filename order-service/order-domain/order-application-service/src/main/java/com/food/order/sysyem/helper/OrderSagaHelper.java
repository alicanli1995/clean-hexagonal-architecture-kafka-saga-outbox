package com.food.order.sysyem.helper;

import com.food.order.system.domain.entity.Order;
import com.food.order.system.domain.exception.OrderNotFoundException;
import com.food.order.sysyem.ports.output.repository.OrderRepository;
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


}
