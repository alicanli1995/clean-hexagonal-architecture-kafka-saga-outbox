package com.food.order.system.order.messaging.publisher.kafka;

import com.food.order.system.domain.event.OrderPaidEvent;
import com.food.order.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.food.order.system.kafka.producer.KafkaMessageHelper;
import com.food.order.system.kafka.producer.service.KafkaProducer;
import com.food.order.system.order.messaging.mapper.OrderMessagingDataMapper;
import com.food.order.sysyem.config.OrderServiceConfigData;
import com.food.order.sysyem.ports.output.message.publisher.restaurantapproval.OrderPaidRestaurantRequestMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PayOrderKafkaMessagePublisher implements OrderPaidRestaurantRequestMessagePublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final OrderServiceConfigData configData;
    private final KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer;
    private final KafkaMessageHelper kafkaMessageHelper;

    @Override
    public void publish(OrderPaidEvent event) {

        var orderId = event.getOrder().getId().getValue().toString();

        try {
            var message =
                    orderMessagingDataMapper.orderPaidEventToRestaurantApprovalRequestAvroModel(event);

            kafkaProducer.send(configData.getRestaurantApprovalRequestTopicName(),
                    orderId,
                    message,
                    kafkaMessageHelper.getKafkaCallBack(configData.getRestaurantApprovalRequestTopicName(), message,
                            orderId,
                            "RestaurantApprovalRequestAvroModel"));

            log.info("Published order paid event for order id: {}", orderId);
        }
        catch (Exception e) {
            log.error("Error publishing order paid event for order id: {}", orderId, e);
        }

    }
}
