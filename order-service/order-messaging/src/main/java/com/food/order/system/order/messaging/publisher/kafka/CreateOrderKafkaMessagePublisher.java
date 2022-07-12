package com.food.order.system.order.messaging.publisher.kafka;


import com.food.order.sysyem.config.OrderServiceConfigData;
import com.food.order.sysyem.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import com.food.order.system.domain.event.OrderCreatedEvent;
import com.food.order.system.kafka.producer.service.KafkaProducer;
import com.food.order.system.order.messaging.mapper.OrderMessagingDataMapper;
import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CreateOrderKafkaMessagePublisher implements OrderCreatedPaymentRequestMessagePublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final OrderServiceConfigData configData;
    private final KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer;
    private final OrderKafkaMessageHelper orderKafkaMessageHelper;


    @Override
    public void publish(OrderCreatedEvent event) {
        var orderId = event.getOrder().getId().getValue().toString();
        log.info("Publishing order created event for order id: {}", orderId);

        try{
            var paymentRequestAvroModel =
                    orderMessagingDataMapper.orderCreatedEventToPaymenRequestAvroModel(event);

            kafkaProducer.send(
                    configData.getPaymentRequestTopicName(),
                    orderId,
                    paymentRequestAvroModel,
                    orderKafkaMessageHelper.getKafkaCallBack(configData.getPaymentRequestTopicName(),
                            paymentRequestAvroModel,
                            orderId,
                            "PaymentRequestAvroModel"));
            log.info("Published order created event for order id: {}", orderId);

        }
        catch(Exception e){
            log.error("Error publishing order created event for order id: {}", orderId, e);
        }

    }

}
