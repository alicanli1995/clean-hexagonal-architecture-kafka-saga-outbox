package com.food.order.system.payment.messaging.publisher.kafka;

import com.food.order.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.order.system.kafka.producer.KafkaMessageHelper;
import com.food.order.system.kafka.producer.service.KafkaProducer;
import com.food.order.system.outbox.OutboxStatus;
import com.food.order.system.payment.application.service.config.PaymentServiceConfigData;
import com.food.order.system.payment.application.service.outbox.model.OrderEventPayload;
import com.food.order.system.payment.application.service.outbox.model.OrderOutboxMessage;
import com.food.order.system.payment.application.service.ports.output.message.publisher.PaymentResponseMessagePublisher;
import com.food.order.system.payment.messaging.mapper.PaymentMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentEventKafkaPublisher implements PaymentResponseMessagePublisher {

    private final PaymentMessagingDataMapper paymentMessagingDataMapper;
    private final KafkaProducer<String , PaymentResponseAvroModel> kafkaProducer;
    private final PaymentServiceConfigData paymentServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;

    @Override
    public void publish(OrderOutboxMessage message,
                        BiConsumer<OrderOutboxMessage, OutboxStatus> outboxCallback) {

        var payload =
                kafkaMessageHelper.getOrderEventPayload(message.getPayload(), OrderEventPayload.class);

        var sagaId = message.getSagaId().toString();

        log.info("Publishing payment response for order id: {}", sagaId);

        try {
            var paymentResponseAvroModel =
                    paymentMessagingDataMapper.orderEventPayloadToPaymentResponseAvroModel(sagaId,payload);

            kafkaProducer.send(paymentServiceConfigData.getPaymentResponseTopicName(),
                    sagaId, paymentResponseAvroModel,
                    kafkaMessageHelper.getKafkaCallback(
                            paymentServiceConfigData.getPaymentResponseTopicName(),
                            paymentResponseAvroModel,
                            message,
                            outboxCallback,
                            payload.getOrderId(),
                            "PaymentResponseAvroModel"
                    ));

            log.info("PaymentResponseAvroModel sent to kafka for order id: {} and saga id: {}",
                    paymentResponseAvroModel.getOrderId(), sagaId);
        }
        catch (Exception e) {
            log.error("Error while publishing payment response for order id: {}", sagaId, e);
        }
    }
}
