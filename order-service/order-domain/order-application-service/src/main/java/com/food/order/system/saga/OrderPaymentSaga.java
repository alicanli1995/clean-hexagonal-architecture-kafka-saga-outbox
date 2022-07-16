package com.food.order.system.saga;

import com.food.order.system.domain.entity.Order;
import com.food.order.system.domain.event.OrderPaidEvent;
import com.food.order.system.domain.exception.OrderDomainException;
import com.food.order.system.domain.service.OrderDomainService;
import com.food.order.system.dto.message.PaymentResponse;
import com.food.order.system.outbox.OutboxStatus;
import com.food.order.system.outbox.model.approval.OrderApprovalOutboxMessage;
import com.food.order.system.outbox.model.payment.OrderPaymentOutboxMessage;
import com.food.order.system.helper.OrderSagaHelper;
import com.food.order.system.mapper.OrderDataMapper;
import com.food.order.system.outbox.scheduler.approval.ApprovalOutboxHelper;
import com.food.order.system.outbox.scheduler.payment.PaymentOutboxHelper;
import com.food.order.system.valueobject.OrderStatus;
import com.food.order.system.valueobject.PaymentStatus;
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
public class OrderPaymentSaga implements SagaStep<PaymentResponse> {

    private final OrderDomainService orderDomainService;
    private final OrderSagaHelper orderSagaHelper;
    private final OrderDataMapper orderDataMapper;
    private final ApprovalOutboxHelper approvalOutboxHelper;
    private final PaymentOutboxHelper paymentOutboxHelper;


    @Override
    @Transactional
    public void process(PaymentResponse data) {

        var messageResponse =
                paymentOutboxHelper.getPaymentOutboxMessageBySagaIdAndSagaStatus(
                UUID.fromString(data.getSagaId()),
                SagaStatus.STARTED)
                        .orElseThrow(() -> {
                            log.error("Payment outbox message not found for saga id: {}", data.getSagaId());
                            return new OrderDomainException("Payment outbox message not found for saga id: " + data.getSagaId());
                        });


        var paidEvent = completePaymentForOrder(data);

        var sagaStatus = orderSagaHelper.orderStatusToSagaStatus(paidEvent.getOrder().getStatus());

        paymentOutboxHelper.save(getUpdatedPaymentOutboxMessage(messageResponse,
                                                                paidEvent.getOrder().getStatus(),
                                                                sagaStatus));

        approvalOutboxHelper.saveApprovalOutboxMessage(
                orderDataMapper.orderPaidEventToOrderApprovalEventPayload(paidEvent),
                paidEvent.getOrder().getStatus(),
                sagaStatus,
                OutboxStatus.STARTED,
                messageResponse.getSagaId()
                );

        log.info("Payment completed for order with id: {}", paidEvent.getOrder().getId().getValue());
    }

    private OrderPaymentOutboxMessage getUpdatedPaymentOutboxMessage(OrderPaymentOutboxMessage messageResponse,
                                                                     OrderStatus status,
                                                                     SagaStatus sagaStatus) {
        messageResponse.setProcessedAt(ZonedDateTime.now(ZoneId.of(UTC)));
        messageResponse.setOrderStatus(status);
        messageResponse.setSagaStatus(sagaStatus);
        return messageResponse;
    }

    @Override
    @Transactional
    public void rollback(PaymentResponse data) {

        var messageResponse =
                paymentOutboxHelper.getPaymentOutboxMessageBySagaIdAndSagaStatus(
                        UUID.fromString(data.getSagaId()),
                        getCurrentSagaStatus(data.getPaymentStatus()))
                        .orElseThrow(
                                () -> {
                                    log.error("Payment outbox message not found for saga id: {}", data.getSagaId());
                                    return new OrderDomainException("Payment outbox message not found for saga id: " + data.getSagaId());
                                }
                        );

        var orderRollback = rollbackPaymentForOrder(data);

        var sagaStatus = orderSagaHelper.orderStatusToSagaStatus(orderRollback.getStatus());

        paymentOutboxHelper.save(getUpdatedPaymentOutboxMessage(messageResponse,
                                                                orderRollback.getStatus(),
                                                                sagaStatus));

        if (data.getPaymentStatus().equals(PaymentStatus.CANCELED)) {
            approvalOutboxHelper.save(getUpdatedApprovalOutboxMessage(data.getSagaId(),
                    orderRollback.getStatus(),
                    sagaStatus));
        }

        log.info("Payment rolled back for order with id: {}", orderRollback.getId());
    }

    private OrderApprovalOutboxMessage getUpdatedApprovalOutboxMessage(String sagaId,
                                                                       OrderStatus orderStatus,
                                                                       SagaStatus sagaStatus) {
        var orderApprovalOutboxMessageResponse =
                approvalOutboxHelper.getApprovalOutboxMessageBySagaIdAndSagaStatus(
                        UUID.fromString(sagaId),
                        SagaStatus.COMPENSATING);
        if (orderApprovalOutboxMessageResponse.isEmpty()) {
            throw new OrderDomainException("Approval outbox message could not be found in " +
                    SagaStatus.COMPENSATING.name() + " status!");
        }
        var orderApprovalOutboxMessage = orderApprovalOutboxMessageResponse.get();
        orderApprovalOutboxMessage.setProcessedAt(ZonedDateTime.now(ZoneId.of(UTC)));
        orderApprovalOutboxMessage.setOrderStatus(orderStatus);
        orderApprovalOutboxMessage.setSagaStatus(sagaStatus);
        return orderApprovalOutboxMessage;
    }

    private Order rollbackPaymentForOrder(PaymentResponse paymentResponse) {
        log.info("Cancelling order with id: {}", paymentResponse.getOrderId());
        var order = orderSagaHelper.findOrder(paymentResponse.getOrderId());
        orderDomainService.cancelOrder(order, paymentResponse.getFailureMessages());
        orderSagaHelper.saveOrder(order);
        return order;
    }

    private SagaStatus[] getCurrentSagaStatus(PaymentStatus paymentStatus) {
        return switch (paymentStatus) {
            case COMPLETED -> new SagaStatus[]{SagaStatus.STARTED};
            case CANCELED -> new SagaStatus[]{SagaStatus.PROCESSING};
            case FAILED -> new SagaStatus[]{SagaStatus.STARTED, SagaStatus.PROCESSING};
        };
    }

    private OrderPaidEvent completePaymentForOrder(PaymentResponse data) {
        var order = orderSagaHelper.findOrder(data.getOrderId());
        var paidEvent = orderDomainService.payOrder(order);
        orderSagaHelper.saveOrder(order);
        log.info("Payment completed for order with id: {}", order.getId());
        return paidEvent;
    }
}
