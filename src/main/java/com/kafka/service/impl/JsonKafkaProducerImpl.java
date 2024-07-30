package com.kafka.service.impl;

import com.kafka.constant.Message;
import com.kafka.dtos.BookDTO;
import com.kafka.service.JsonKafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class JsonKafkaProducerImpl implements JsonKafkaProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void sendMessage(BookDTO book) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate
                .send("topicOne", book);

        future.thenAccept(sendResult -> {
            log.info(Message.PAYLOAD_PARTITION_OFFSET,
                    book.toString(),
                    sendResult.getRecordMetadata().toString());
        }).exceptionally(throwable -> {
            log.error(Message.PAYLOAD_PARTITION_OFFSET,
                    book.toString(),
                    "topicTwo",
                    throwable.toString());
            return null;
        });
    }
}
