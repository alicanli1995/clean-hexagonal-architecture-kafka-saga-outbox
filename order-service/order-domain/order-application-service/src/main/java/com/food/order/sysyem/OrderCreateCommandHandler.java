package com.food.order.sysyem;

import com.food.order.sysyem.dto.create.CreateOrderCommand;
import com.food.order.sysyem.dto.create.CreateOrderResponse;
import com.food.order.sysyem.helper.OrderCreateHelper;
import com.food.order.sysyem.mapper.OrderDataMapper;
import com.food.order.sysyem.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderCreateCommandHandler {

    private final OrderCreateHelper orderCreateHelper;
    private final OrderDataMapper orderDataMapper;

    private final OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher;


    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        var persistOrder = orderCreateHelper.persistOrder(createOrderCommand);
        log.info("createOrder with id: {}", persistOrder.getOrder().getId().getValue());
        orderCreatedPaymentRequestMessagePublisher.publish(persistOrder);
        return orderDataMapper.orderToCreateOrderResponse(persistOrder.getOrder(),"Order created successfully");
    }



}
