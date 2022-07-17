package com.food.order.system.messaging.listener.kafka;

import com.food.order.system.kafka.consumer.KafkaConsumer;
import com.food.order.system.kafka.order.avro.model.CustomerAvroModel;
import com.food.order.system.messaging.mapper.OrderMessagingDataMapper;
import com.food.order.system.ports.input.message.listener.customer.CustomerMessageListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerKafkaListener implements KafkaConsumer<CustomerAvroModel> {

    private final CustomerMessageListener customerMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;

    @Override
    @KafkaListener(id = "${kafka-consumer-config.customer-group-id}",
            topics = "${order-service.customer-topic-name}")
    public void receive(List<CustomerAvroModel> messages,
                        List<String> keys,
                        List<Integer> partitions,
                        List<Long> offSets) {

        log.info("{} number of customer create messages received with keys {}, partitions {} and offsets {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offSets.toString());

        messages.forEach(customerAvroModel ->
                customerMessageListener.customerCreated(orderMessagingDataMapper
                        .customerAvroModelToCustomerModel(customerAvroModel)));


    }
}
