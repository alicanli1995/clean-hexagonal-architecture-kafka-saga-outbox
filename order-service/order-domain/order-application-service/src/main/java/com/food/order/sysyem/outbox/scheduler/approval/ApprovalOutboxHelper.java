package com.food.order.sysyem.outbox.scheduler.approval;

import com.food.order.system.domain.exception.OrderDomainException;
import com.food.order.system.outbox.OutboxStatus;
import com.food.order.system.saga.SagaStatus;
import com.food.order.sysyem.outbox.model.approval.OrderApprovalOutboxMessage;
import com.food.order.sysyem.ports.output.repository.ApprovalOutboxRepository;
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
@RequiredArgsConstructor
@Component
public class ApprovalOutboxHelper {

    private final ApprovalOutboxRepository approvalOutboxRepository;

    @Transactional(readOnly = true)
    public Optional<List<OrderApprovalOutboxMessage>> getApprovalOutboxMessageByOutboxStatusAndSagaStatus
            (OutboxStatus outboxStatus, SagaStatus... sagaStatus) {
        return approvalOutboxRepository.findByTypeAndOutboxStatusAndSagaStatus(
                ORDER_PROCESSING_SAGA
                ,outboxStatus,
                sagaStatus);
    }


    @Transactional(readOnly = true)
    public Optional<OrderApprovalOutboxMessage> getApprovalOutboxMessageBySagaIdAndSagaStatus(
            UUID sagaId, SagaStatus... sagaStatus) {
        return approvalOutboxRepository.findByTypeAndSagaIdAndSagaStatus(
                ORDER_PROCESSING_SAGA
                ,sagaId,
                sagaStatus);
    }

    @Transactional
    public void save(OrderApprovalOutboxMessage approvalOutboxMessage) {
        var response = approvalOutboxRepository.save(approvalOutboxMessage);
        if (Objects.isNull(response)) {
            throw new OrderDomainException("Failed to save outbox message id : " +
                    approvalOutboxMessage.getId());
        }
        log.info("Outbox message id : {} saved successfully", response.getId());
    }

    @Transactional
    public void deleteApprovalOutboxMessageByOutboxStatusAndSagaStatus(OutboxStatus outboxStatus,
                                                                       SagaStatus... sagaStatus) {

        approvalOutboxRepository.deleteByTypeAndOutboxStatusAndSagaStatus(
                ORDER_PROCESSING_SAGA,
                outboxStatus,
                sagaStatus);

    }
}