package com.food.order.system.outbox.scheduler.approval;

import com.food.order.system.domain.exception.OrderDomainException;
import com.food.order.system.outbox.OutboxScheduler;
import com.food.order.system.outbox.OutboxStatus;
import com.food.order.system.outbox.model.approval.OrderApprovalOutboxMessage;
import com.food.order.system.saga.SagaStatus;
import com.food.order.system.ports.output.message.publisher.restaurantapproval.RestaurantApprovalRequestMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestaurantApprovalOutboxScheduler implements OutboxScheduler {

    private final ApprovalOutboxHelper approvalOutboxHelper;
    private final RestaurantApprovalRequestMessagePublisher restaurantApprovalRequestMessagePublisher;

    @Override
    @Transactional
    @Scheduled(fixedDelayString = "${order-service.outbox-scheduler-fixed-rate}",
            initialDelayString = "${order-service.outbox-scheduler-initial-delay}")
    public void processOutboxMessage() {

        log.info("Processing outbox message STARTED !");

        var outboxMessageResponse =
                approvalOutboxHelper.getApprovalOutboxMessageByOutboxStatusAndSagaStatus(
                OutboxStatus.STARTED,
                SagaStatus.STARTED,
                SagaStatus.COMPENSATING)
                        .orElseThrow(
                                () -> new OrderDomainException("No outbox message found for processing"));

        if (Objects.nonNull(outboxMessageResponse) && outboxMessageResponse.size() > 0) {

            log.info("Received {} OrderPaymentOutboxMessage with ids :  {} , sending message bus !" ,
                    outboxMessageResponse.size(),
                    outboxMessageResponse.stream().map(orderPaymentOutboxMessage -> orderPaymentOutboxMessage.getId().toString())
                            .collect(Collectors.joining(",")));
            outboxMessageResponse.forEach(orderPaymentOutboxMessage -> {
                restaurantApprovalRequestMessagePublisher.publish
                        (orderPaymentOutboxMessage,this::updateOutboxStatus);
            });
            log.info("Processing outbox message completed ! ");
        }

    }


    private void updateOutboxStatus(OrderApprovalOutboxMessage orderPaymentOutboxMessage,
                                    OutboxStatus outboxStatus) {
        orderPaymentOutboxMessage.setOutboxStatus(outboxStatus);
        approvalOutboxHelper.save(orderPaymentOutboxMessage);
        log.info("Outbox message id : {} updated successfully", orderPaymentOutboxMessage.getId());
    }
}
