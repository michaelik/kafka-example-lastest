package com.kafka.service;

import com.kafka.dtos.BookDTO;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

public interface JsonKafkaConsumer {
    void handleBook(
            @Payload BookDTO payload,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) Long offset,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic);
    void handleDlqMessage(String payload);
}
