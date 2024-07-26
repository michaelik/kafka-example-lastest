package com.kafka.service;

import com.kafka.dtos.Book;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

public interface JsonKafkaConsumer {

    @KafkaListener(topics = "topicOne", groupId = "myGroup")
    void handleBook(
            @Payload Book book,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) Long offset,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic);
}
