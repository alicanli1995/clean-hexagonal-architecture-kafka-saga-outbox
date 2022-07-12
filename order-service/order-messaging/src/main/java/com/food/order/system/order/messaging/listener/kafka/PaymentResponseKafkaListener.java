package com.food.order.system.order.messaging.listener.kafka;

import com.food.order.sysyem.ports.input.message.listener.payment.PaymentResponseMessageListener;
import com.food.order.system.kafka.consumer.KafkaConsumer;
import com.food.order.system.order.messaging.mapper.OrderMessagingDataMapper;
import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.ordering.system.kafka.order.avro.model.PaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentResponseKafkaListener implements KafkaConsumer<PaymentResponseAvroModel> {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final PaymentResponseMessageListener paymentResponseMessageListener;

    @Override
    @KafkaListener(
            id = "${kafka-consumer-config.payment-consumer-group-id}",
            topics = "${order-service.payment-response-topic-name}"
    )
    public void receive(@Payload List<PaymentResponseAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offSets) {

        log.info("{} number of payment responses received with keys : {} , partitions : {} , offsets : {}",
                messages.size(), keys, partitions, offSets);

        messages.forEach(message -> {
            if (PaymentStatus.COMPLETED.equals(message.getPaymentStatus())) {
                log.info("Processing successful payment response for order id: {}", message.getOrderId());
                paymentResponseMessageListener.paymentCompleted(orderMessagingDataMapper.
                        paymentResponseAvroModelToPaymentResponse(message));
            }
            else if (PaymentStatus.FAILED.equals(message.getPaymentStatus()) ||
                     PaymentStatus.CANCELLED.equals(message.getPaymentStatus())) {
                log.info("Processing failed payment response for order id: {}", message.getOrderId());
                paymentResponseMessageListener.paymentCancelled(orderMessagingDataMapper.
                        paymentResponseAvroModelToPaymentResponse(message));
            }
        });

    }
}
