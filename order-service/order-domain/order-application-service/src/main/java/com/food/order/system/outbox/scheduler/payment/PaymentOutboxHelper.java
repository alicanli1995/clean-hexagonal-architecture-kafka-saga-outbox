package com.food.order.system.outbox.scheduler.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.order.system.domain.exception.OrderDomainException;
import com.food.order.system.outbox.OutboxStatus;
import com.food.order.system.outbox.model.payment.OrderPaymentEventPayload;
import com.food.order.system.outbox.model.payment.OrderPaymentOutboxMessage;
import com.food.order.system.ports.output.repository.PaymentOutboxRepository;
import com.food.order.system.saga.SagaStatus;
import com.food.order.system.valueobject.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.food.order.system.outbox.order.SagaConst.ORDER_PROCESSING_SAGA;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentOutboxHelper {

    private final PaymentOutboxRepository paymentOutboxRepository;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public Optional<List<OrderPaymentOutboxMessage>> getPaymentOutboxMessageByOutboxMessageStatusAndSagaStatus(
            OutboxStatus outboxStatus, SagaStatus... sagaStatus) {
        return paymentOutboxRepository.findByTypeAndOutboxStatusAndSagaStatus(
                ORDER_PROCESSING_SAGA,
                outboxStatus,
                sagaStatus);

    }

    @Transactional(readOnly = true)
    public Optional<OrderPaymentOutboxMessage> getPaymentOutboxMessageBySagaIdAndSagaStatus(
            UUID sagaId, SagaStatus... sagaStatus) {
        return paymentOutboxRepository.findByTypeAndSagaIdAndSagaStatus(
                ORDER_PROCESSING_SAGA
                ,sagaId,
                sagaStatus);
    }

    @Transactional
    public void save(OrderPaymentOutboxMessage orderPaymentOutboxMessage) {
        var response = paymentOutboxRepository.save(orderPaymentOutboxMessage);
        if (Objects.isNull(response)) {
            throw new OrderDomainException("Failed to save outbox message id : " +
                    orderPaymentOutboxMessage.getId());
        }
        log.info("Outbox message id : {} saved successfully", response.getId());
    }

    @Transactional
    public void savePaymentOutboxMessage(OrderPaymentEventPayload payload,
                                         OrderStatus orderStatus,
                                         SagaStatus sagaStatus,
                                         OutboxStatus outboxStatus,
                                         UUID sagaId) {

        save(OrderPaymentOutboxMessage.builder()
                .id(UUID.randomUUID())
                .sagaId(sagaId)
                .createdAt(payload.getCreatedAt())
                .type(ORDER_PROCESSING_SAGA)
                .payload(createPayload(payload))
                .outboxStatus(outboxStatus)
                .orderStatus(orderStatus)
                .sagaStatus(sagaStatus)
                .build());

    }

    private String createPayload(OrderPaymentEventPayload payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            log.error("Failed to create payload for outbox message", e);
            throw new OrderDomainException("Failed to create payload for outbox message");
        }
    }

    @Transactional
    public void deletePaymentOutboxMessageByOutboxStatusAndSagaStatus(OutboxStatus outboxStatus,
                                                                      SagaStatus... sagaStatus) {

        paymentOutboxRepository.deleteByTypeAndOutboxStatusAndSagaStatus(
                ORDER_PROCESSING_SAGA,
                outboxStatus,
                sagaStatus);

    }
}
