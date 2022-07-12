package com.food.order.system.order.messaging.publisher.kafka;

import com.food.order.sysyem.config.OrderServiceConfigData;
import com.food.order.sysyem.ports.output.message.publisher.payment.OrderCancelledPaymentRequestMessagePublisher;
import com.food.order.system.domain.event.OrderCancelledEvent;
import com.food.order.system.kafka.producer.service.KafkaProducer;
import com.food.order.system.order.messaging.mapper.OrderMessagingDataMapper;
import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class CancelOrderKafkaMessagePublisher implements OrderCancelledPaymentRequestMessagePublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final OrderServiceConfigData configData;
    private final KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer;

    private final OrderKafkaMessageHelper orderKafkaMessageHelper;

    @Override
    public void publish(OrderCancelledEvent event) {

        var orderId = event.getOrder().getId().getValue().toString();
        log.info("Publishing order cancel event for order id: {}", orderId);

        try{
            var paymentRequestAvroModel =
                    orderMessagingDataMapper.orderCancelledEventToPaymenRequestAvroModel(event);

            kafkaProducer.send(
                    configData.getPaymentRequestTopicName(),
                    orderId,
                    paymentRequestAvroModel,
                    orderKafkaMessageHelper.getKafkaCallBack(configData.getPaymentRequestTopicName()
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
