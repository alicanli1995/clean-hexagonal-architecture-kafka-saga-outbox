package com.food.order.system.messaging.listener.kafka;

import com.food.order.system.kafka.consumer.KafkaConsumer;
import com.food.order.system.kafka.order.avro.model.OrderApprovalStatus;
import com.food.order.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.order.system.messaging.mapper.OrderMessagingDataMapper;
import com.food.order.system.ports.input.message.listener.restaurantapproval.RestaurantApprovalResponseMessageListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class RestaurantApprovalResponseKafkaListener implements KafkaConsumer<RestaurantApprovalResponseAvroModel> {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final RestaurantApprovalResponseMessageListener responseMessageListener;

    @Override
    @KafkaListener(
            id = "${kafka-consumer-config.restaurant-approval-consumer-group-id}",
            topics = "${order-service.restaurant-approval-response-topic-name}"
    )
    public void receive(@Payload List<RestaurantApprovalResponseAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offSets) {

        log.info("{} number of restaurant approval responses received with keys : {} , partitions : {} , offsets : {}",
                messages.size(), keys, partitions, offSets);

        messages.forEach(message -> {
            if (message.getOrderApprovalStatus().equals(OrderApprovalStatus.APPROVED)) {
                log.info("Processing successful restaurant approval response for order id: {}", message.getOrderId());
                responseMessageListener.orderApproved(orderMessagingDataMapper.
                        restaurantApprovalResponseAvroModelToRestaurantApprovalResponse(message));
            }
            else if (message.getOrderApprovalStatus().equals(OrderApprovalStatus.REJECTED)) {
                log.info("Processing failed restaurant approval response for order id: {}", message.getOrderId());
                responseMessageListener.orderRejected(orderMessagingDataMapper.
                        restaurantApprovalResponseAvroModelToRestaurantApprovalResponse(message));
            }
        });
    }
}
