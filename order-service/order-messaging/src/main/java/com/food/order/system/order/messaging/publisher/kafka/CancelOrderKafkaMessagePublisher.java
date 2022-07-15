package com.food.order.system.order.messaging.publisher.kafka;

import com.food.order.system.domain.event.OrderCancelledEvent;
import com.food.order.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.order.system.kafka.producer.KafkaMessageHelper;
import com.food.order.system.kafka.producer.service.KafkaProducer;
import com.food.order.system.order.messaging.mapper.OrderMessagingDataMapper;
import com.food.order.sysyem.config.OrderServiceConfigData;
import com.food.order.sysyem.event.publisher.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class CancelOrderKafkaMessagePublisher implements DomainEventPublisher<OrderCancelledEvent> {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final OrderServiceConfigData configData;
    private final KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer;

    private final KafkaMessageHelper kafkaMessageHelper;

    @Override
    public void publish(OrderCancelledEvent event) {

        var orderId = event.getOrder().getId().getValue().toString();
        log.info("Publishing order cancel event for order id: {}", orderId);

        try{
            var paymentRequestAvroModel =
                    orderMessagingDataMapper.orderCancelledEventToPaymentRequestAvroModel(event);

            kafkaProducer.send(
                    configData.getPaymentRequestTopicName(),
                    orderId,
                    paymentRequestAvroModel,
                    kafkaMessageHelper.getKafkaCallBack(configData.getPaymentRequestTopicName()
                            ,paymentRequestAvroModel,
                            orderId,
                            "PaymentRequestAvroModel"));
            log.info("Published order cancel event for order id: {}", orderId);

        }
        catch(Exception e){
            log.error("Error publishing order cancel event for order id: {}", orderId, e);
        }


    }


}
