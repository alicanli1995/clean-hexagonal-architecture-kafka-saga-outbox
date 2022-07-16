package com.food.order.system.outbox.scheduler.payment;

import com.food.order.system.domain.exception.OrderDomainException;
import com.food.order.system.outbox.OutboxScheduler;
import com.food.order.system.outbox.OutboxStatus;
import com.food.order.system.outbox.model.payment.OrderPaymentOutboxMessage;
import com.food.order.system.saga.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentOutboxCleaner implements OutboxScheduler {

    private final PaymentOutboxHelper paymentOutboxHelper;

    @Override
    @Scheduled(cron = "@midnight")
    public void processOutboxMessage() {

        var outboxMessageResponse =
                paymentOutboxHelper.getPaymentOutboxMessageByOutboxMessageStatusAndSagaStatus(
                OutboxStatus.COMPLETED,
                SagaStatus.SUCCEEDED,
                SagaStatus.FAILED,
                SagaStatus.COMPENSATING).
                orElseThrow(() -> new OrderDomainException("No outbox message found for processing"));

        if (Objects.nonNull(outboxMessageResponse)) {

            log.info("Received {} OrderPaymentOutboxMessage for clean-up. The Payloads :{}",
                    outboxMessageResponse.size(),
                    outboxMessageResponse.stream().map(OrderPaymentOutboxMessage::getPayload)
                            .collect(Collectors.joining(",")));
            paymentOutboxHelper.deletePaymentOutboxMessageByOutboxStatusAndSagaStatus(
                    OutboxStatus.COMPLETED,
                    SagaStatus.SUCCEEDED,
                    SagaStatus.FAILED,
                    SagaStatus.COMPENSATING);
            log.info("Clean-up completed ! DELETED LOG SIZE : {}", outboxMessageResponse.size());

        }

    }
}
