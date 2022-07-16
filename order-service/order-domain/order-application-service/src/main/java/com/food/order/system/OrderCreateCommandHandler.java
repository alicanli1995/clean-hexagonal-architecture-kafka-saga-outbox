package com.food.order.system;

import com.food.order.system.dto.create.CreateOrderCommand;
import com.food.order.system.dto.create.CreateOrderResponse;
import com.food.order.system.outbox.OutboxStatus;
import com.food.order.system.helper.OrderCreateHelper;
import com.food.order.system.helper.OrderSagaHelper;
import com.food.order.system.mapper.OrderDataMapper;
import com.food.order.system.outbox.scheduler.payment.PaymentOutboxHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderCreateCommandHandler {

    private final OrderCreateHelper orderCreateHelper;
    private final OrderDataMapper orderDataMapper;

    private final OrderSagaHelper orderSagaHelper;
    private final PaymentOutboxHelper paymentOutboxHelper;



    @Transactional
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        var persistOrder = orderCreateHelper.persistOrder(createOrderCommand);
        log.info("createOrder with id: {}", persistOrder.getOrder().getId().getValue());
        var response = orderDataMapper.orderToCreateOrderResponse(persistOrder.getOrder(),"Order created successfully");

        paymentOutboxHelper.savePaymentOutboxMessage(
                orderDataMapper.orderCreatedEventToOrderPaymentEventPayload(persistOrder),
                persistOrder.getOrder().getStatus(),
                orderSagaHelper.orderStatusToSagaStatus(persistOrder.getOrder().getStatus()),
                OutboxStatus.STARTED,
                UUID.randomUUID()
                );

        log.info("Returning CreateOrderResponse with order id : {}", persistOrder.getOrder().getId());

        return response;
    }



}
