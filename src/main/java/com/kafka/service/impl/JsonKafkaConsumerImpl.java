package com.kafka.service.impl;

import com.kafka.constant.Message;
import com.kafka.dtos.Book;
import com.kafka.service.JsonKafkaConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JsonKafkaConsumerImpl implements JsonKafkaConsumer {
    @Override
    public void handleBook(Book  payload, int partition, Long offset, String topic) {
        log.info(Message.PARTITION_DETAILS.formatted(payload, partition, offset, topic));
    }
}
