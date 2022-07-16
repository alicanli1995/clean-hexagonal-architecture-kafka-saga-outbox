package com.food.order.system.saga;

import com.food.order.system.domain.entity.Order;
import com.food.order.system.domain.event.OrderCancelledEvent;
import com.food.order.system.domain.exception.OrderDomainException;
import com.food.order.system.domain.service.OrderDomainService;
import com.food.order.system.helper.OrderSagaHelper;
import com.food.order.system.mapper.OrderDataMapper;
import com.food.order.system.outbox.OutboxStatus;
import com.food.order.system.outbox.model.approval.OrderApprovalOutboxMessage;
import com.food.order.system.outbox.scheduler.approval.ApprovalOutboxHelper;
import com.food.order.system.outbox.scheduler.payment.PaymentOutboxHelper;
import com.food.order.system.dto.message.RestaurantApprovalResponse;
import com.food.order.system.valueobject.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import static com.food.order.system.DomainConstants.UTC;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderApprovalSaga implements SagaStep<RestaurantApprovalResponse> {

    private final OrderDomainService orderDomainService;
    private final OrderSagaHelper orderSagaHelper;
    private final PaymentOutboxHelper paymentOutboxHelper;
    private final OrderDataMapper orderDataMapper;
    private final ApprovalOutboxHelper approvalOutboxHelper;



    @Override
    @Transactional
    public void process(RestaurantApprovalResponse data) {

        var messageResponse =
                approvalOutboxHelper.getApprovalOutboxMessageBySagaIdAndSagaStatus(
                        UUID.fromString(data.getSagaId()),
                        SagaStatus.PROCESSING)
                        .orElseThrow(() -> {
                            log.error("Approval outbox message not found for saga id: {}", data.getSagaId());
                                return new OrderDomainException("Approval outbox message not found for saga id: " + data.getSagaId());
                        });

        var order = approveOrder(data);

        var sagaStatus = orderSagaHelper.orderStatusToSagaStatus(order.getStatus());

        approvalOutboxHelper.save(getUpdatedApprovalOutboxMessage(messageResponse,order.getStatus(), sagaStatus));

        approvalOutboxHelper.save(getUpdatedPaymentOutboxMessage(messageResponse.getSagaId(),
                order.getStatus(), sagaStatus));



        log.info("Order approved: {}", order);
    }

    private OrderApprovalOutboxMessage getUpdatedPaymentOutboxMessage(UUID sagaId,
                                                                      OrderStatus status,
                                                                      SagaStatus sagaStatus) {
        var message =  approvalOutboxHelper.getApprovalOutboxMessageBySagaIdAndSagaStatus(
                sagaId,
                SagaStatus.PROCESSING)
                .orElseThrow(() -> {
                    log.error("Approval outbox message not found for saga id: {}", sagaId);
                    return new OrderDomainException("Approval outbox message not found for saga id: " + sagaId);
                });

        message.setProcessedAt(ZonedDateTime.now(ZoneId.of(UTC)));
        message.setSagaStatus(sagaStatus);
        message.setOrderStatus(status);
        return message;
    }

    private OrderApprovalOutboxMessage getUpdatedApprovalOutboxMessage(OrderApprovalOutboxMessage messageResponse,
                                                                       OrderStatus status,
                                                                       SagaStatus sagaStatus) {
        messageResponse.setProcessedAt(ZonedDateTime.now(ZoneId.of(UTC)));
        messageResponse.setSagaStatus(sagaStatus);
        messageResponse.setOrderStatus(status);
        return messageResponse;
    }


    private Order approveOrder(RestaurantApprovalResponse data) {
        var order = orderSagaHelper.findOrder(data.getOrderId());
        orderDomainService.approve(order);
        orderSagaHelper.saveOrder(order);
        return order;
    }


    @Override
    @Transactional
    public void rollback(RestaurantApprovalResponse data) {

        var message = approvalOutboxHelper.getApprovalOutboxMessageBySagaIdAndSagaStatus(
                UUID.fromString(data.getSagaId()),
                SagaStatus.PROCESSING)
                .orElseThrow(
                        () -> {
                            log.error("Approval outbox message not found for saga id: {}", data.getSagaId());
                            return new OrderDomainException("Approval outbox message not found for saga id: " +
                                    data.getSagaId());
                        }
                );

        var event = rollbackOrder(data);

        var sagaStatus = orderSagaHelper.orderStatusToSagaStatus(event.getOrder().getStatus());

        approvalOutboxHelper.save(getUpdatedApprovalOutboxMessage(message, event.getOrder().getStatus(), sagaStatus));

        paymentOutboxHelper.savePaymentOutboxMessage(
                orderDataMapper.orderCancelledEventToOrderPaymentEventPayload(event),
                event.getOrder().getStatus(),
                sagaStatus,
                OutboxStatus.STARTED,
                message.getSagaId()
        );

        log.info("Order cancelled event id: {}", event.getOrder().getId());
    }

    private OrderCancelledEvent rollbackOrder(RestaurantApprovalResponse data) {
        var order = orderSagaHelper.findOrder(data.getOrderId());
        var event = orderDomainService.cancelOrderPayment(
                order,
                data.getFailureMessages());
        orderSagaHelper.saveOrder(order);
        return event;
    }
}
