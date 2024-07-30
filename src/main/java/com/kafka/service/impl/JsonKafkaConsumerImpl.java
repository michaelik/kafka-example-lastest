package com.kafka.service.impl;

import com.kafka.config.KafkaTopicConfig;
import com.kafka.constant.Message;
import com.kafka.dtos.BookDTO;
import com.kafka.service.JsonKafkaConsumer;
import jakarta.mail.SendFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.TimeoutException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.messaging.MessagingException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class JsonKafkaConsumerImpl implements JsonKafkaConsumer {

    private final KafkaTopicConfig kafkaTopicConfig;


    @RetryableTopic(
            include = {
                    SendFailedException.class,
                    MessagingException.class,
                    TimeoutException.class,
                    IOException.class
            },
            attempts = "4",
            backoff = @Backoff(delay = 1000, multiplier = 2),
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_DELAY_VALUE,
            retryTopicSuffix = "-retry",
            dltTopicSuffix = "-dlt"
    )
    @KafkaListener(topics = "#{@kafkaTopicConfig.getTopics()[0]}")
    @Override
    public void handleBook(BookDTO payload, int partition, Long offset, String topic) {
        try {
            // Processing logic for the Book payload
            log.info(Message.PARTITION_DETAILS.formatted(payload, partition, offset, topic));
            // Add your email sending or processing logic here

        } catch (Exception e) {
            log.error(Message.ERROR_PROCESSING_MSG, topic, e.getMessage());
            throw e; // This will trigger the retry mechanism
        }
    }

    @KafkaListener(topics = "#{@kafkaTopicConfig.getTopics()[1]}", groupId = "dltGroup")
    public void handleDlqMessage(String payload) {
        // Process or analyze the failed message
        log.error(Message.DLT_MESSAGE_RECEIVED, payload);
    }


}
