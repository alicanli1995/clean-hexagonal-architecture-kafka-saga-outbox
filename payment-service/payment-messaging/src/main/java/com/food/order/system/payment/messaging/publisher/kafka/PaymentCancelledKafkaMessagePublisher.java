package com.food.order.system.payment.messaging.publisher.kafka;

import com.food.order.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.order.system.kafka.producer.KafkaMessageHelper;
import com.food.order.system.kafka.producer.service.KafkaProducer;
import com.food.order.system.payment.application.service.config.PaymentServiceConfigData;
import com.food.order.system.payment.application.service.ports.output.message.publisher.PaymentCancelledMessagePublisher;
import com.food.order.system.payment.messaging.mapper.PaymentMessagingDataMapper;
import com.food.order.system.payment.service.domain.event.PaymentCancelledEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentCancelledKafkaMessagePublisher implements PaymentCancelledMessagePublisher {

    private final PaymentMessagingDataMapper paymentDataMapper;
    private final KafkaProducer<String , PaymentResponseAvroModel> kafkaProducer;
    private final PaymentServiceConfigData paymentServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;


    @Override
    public void publish(PaymentCancelledEvent event) {
        log.info("Publishing payment cancelled event to kafka");
        var orderId = event.getPayment().getOrderId().getValue().toString();
        try {
            var paymentResponseAvroModel =
                    paymentDataMapper.paymentCancelEventToPaymentResponseAvroModel(event);

            kafkaProducer.send(paymentServiceConfigData.getPaymentResponseTopicName(),
                    orderId,
                    paymentResponseAvroModel,
                    kafkaMessageHelper.getKafkaCallBack(
                            paymentServiceConfigData.getPaymentResponseTopicName(),
                            paymentResponseAvroModel,
                            orderId,
                            "PaymentResponseAvroModel"));

            log.info("Published payment cancelled event to kafka");
        }   catch (Exception e) {
            log.error("Error while publishing payment cancelled event to kafka", e);
        }
    }
}
