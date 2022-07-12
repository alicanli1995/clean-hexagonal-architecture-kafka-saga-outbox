package com.food.order.system.kafka.consumer;

import org.apache.avro.specific.SpecificRecordBase;

import java.util.List;

public interface KafkaConsumer<T extends SpecificRecordBase> {
    void receive(List<T> messages , List<String> keys , List<Integer> partitions , List<Long> offSets);
}
