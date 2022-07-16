package com.food.order.system.outbox.scheduler.payment;

import com.food.order.system.domain.exception.OrderDomainException;
import com.food.order.system.outbox.OutboxScheduler;
import com.food.order.system.outbox.OutboxStatus;
import com.food.order.system.outbox.model.payment.OrderPaymentOutboxMessage;
import com.food.order.system.ports.output.message.publisher.payment.PaymentRequestMessagePublisher;
import com.food.order.system.saga.SagaStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
//@RequiredArgsConstructor
public class PaymentOutboxScheduler implements OutboxScheduler {

    private final PaymentOutboxHelper paymentOutboxHelper;
    private final PaymentRequestMessagePublisher paymentRequestMessagePublisher;

    public PaymentOutboxScheduler(PaymentOutboxHelper paymentOutboxHelper, PaymentRequestMessagePublisher paymentRequestMessagePublisher) {
        this.paymentOutboxHelper = paymentOutboxHelper;
        this.paymentRequestMessagePublisher = paymentRequestMessagePublisher;
    }

    @Override
    @Transactional
    @Scheduled(fixedDelayString = "${order-service.outbox-scheduler-fixed-rate}",
            initialDelayString = "${order-service.outbox-scheduler-initial-delay}")
    public void processOutboxMessage() {

        log.info("Processing outbox message STARTED !");

        var outboxMessageResponse =
                paymentOutboxHelper.getPaymentOutboxMessageByOutboxMessageStatusAndSagaStatus(
                OutboxStatus.STARTED,
                SagaStatus.STARTED,
                SagaStatus.COMPENSATING)
                        .orElseThrow(
                                () -> new OrderDomainException("No outbox message found for processing"));

        if (Objects.nonNull(outboxMessageResponse) && !outboxMessageResponse.isEmpty()) {

            log.info("Received {} OrderPaymentOutboxMessage with ids :  {} , sending message bus !" ,
                    outboxMessageResponse.size(),
                    outboxMessageResponse.stream().map(orderPaymentOutboxMessage -> orderPaymentOutboxMessage.getId().toString())
                            .collect(Collectors.joining(",")));
            outboxMessageResponse.forEach(orderPaymentOutboxMessage -> {
                paymentRequestMessagePublisher.publish(orderPaymentOutboxMessage,this::updateOutboxStatus);
            });
            log.info("Processing outbox message completed ! ");
        }

    }


    private void updateOutboxStatus(OrderPaymentOutboxMessage orderPaymentOutboxMessage,
                                    OutboxStatus outboxStatus) {
        orderPaymentOutboxMessage.setOutboxStatus(outboxStatus);
        paymentOutboxHelper.save(orderPaymentOutboxMessage);
        log.info("Outbox message id : {} updated successfully", orderPaymentOutboxMessage.getId());
    }
}
