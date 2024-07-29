package com.kafka.service.impl;

import com.kafka.constant.Message;
import com.kafka.dtos.Book;
import com.kafka.service.JsonKafkaConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class JsonKafkaConsumerImpl implements JsonKafkaConsumer {

    @Value("#{'${kafka.topics.user.name}'.split(',')}")
    private List<String> topics;


    /*
    * The syntax #{__listener.topics[0]} is used within the
    * @KafkaListener annotation because it leverages Spring
    * Expression Language (SpEL) to dynamically reference
    * a bean property at runtime.
    * */
    @KafkaListener(topics = "#{__listener.topics[0]}")
    @Override
    public void handleBook(Book  payload, int partition, Long offset, String topic) {
        log.info(Message.PARTITION_DETAILS.formatted(payload, partition, offset, topic));
    }
}
