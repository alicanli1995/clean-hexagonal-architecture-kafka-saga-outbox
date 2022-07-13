package com.food.order.system.payment.messaging.publisher.kafka;

import com.food.order.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.order.system.kafka.producer.KafkaMessageHelper;
import com.food.order.system.kafka.producer.service.KafkaProducer;
import com.food.order.system.payment.application.service.config.PaymentServiceConfigData;
import com.food.order.system.payment.application.service.ports.output.message.publisher.PaymentCompletedMessagePublisher;
import com.food.order.system.payment.messaging.mapper.PaymentMessagingDataMapper;
import com.food.order.system.payment.service.domain.event.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentCompletedKafkaMessagePublisher implements PaymentCompletedMessagePublisher {

    private final PaymentMessagingDataMapper paymentDataMapper;
    private final KafkaProducer<String , PaymentResponseAvroModel> kafkaProducer;
    private final PaymentServiceConfigData paymentServiceConfigData;

    private final KafkaMessageHelper kafkaMessageHelper;


    @Override
    public void publish(PaymentCompletedEvent event) {
        log.info("Publishing payment completed event to kafka");
        var orderId = event.getPayment().getOrderId().getValue().toString();
        try {
            var paymentResponseAvroModel =
                    paymentDataMapper.paymentCompletedEventToPaymentResponseAvroModel(event);

            kafkaProducer.send(paymentServiceConfigData.getPaymentResponseTopicName(),
                    orderId,
                    paymentResponseAvroModel,
                    kafkaMessageHelper.getKafkaCallBack(
                            paymentServiceConfigData.getPaymentResponseTopicName(),
                            paymentResponseAvroModel,
                            orderId,
                            "PaymentResponseAvroModel"));

            log.info("Published payment completed event to kafka");
        }   catch (Exception e) {
            log.error("Error while publishing payment completed event to kafka", e);
        }
    }
}
