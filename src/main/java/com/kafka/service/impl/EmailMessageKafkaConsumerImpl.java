package com.kafka.service.impl;

import com.kafka.config.KafkaTopicConfig;
import com.kafka.constant.Message;
import com.kafka.dtos.EmailDTO;
import com.kafka.entity.EmailMessage;
import com.kafka.mappers.EmailMessageMapper;
import com.kafka.repository.EmailMessageRepository;
import com.kafka.service.EmailMessageKafkaConsumer;
import com.kafka.service.EmailMessageSenderService;
import jakarta.annotation.PreDestroy;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailMessageKafkaConsumerImpl implements EmailMessageKafkaConsumer {

    private final KafkaTopicConfig kafkaTopicConfig;
    private final EmailMessageSenderService emailMessageSenderService;
    private final EmailMessageRepository emailMessageRepository;
    private final ExecutorService executorService = Executors.newFixedThreadPool(20);
    private final Semaphore semaphore = new Semaphore(30);


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
    public void listen(Long id, int partition, Long offset, String topic)
            throws InterruptedException {
        try {
            EmailDTO emailDTO = getEmailMessageDTO(id);
            logMessageDetails(id, partition, offset, topic);
            // Add your email sending or processing logic here
            semaphore.acquire();
            executorService.submit(() -> {
                try {
                    emailMessageSenderService.sendEmail(emailDTO);
                } catch (Exception e) {
                    log.error(Message.ERROR_SENDING_EMAIL, e.getMessage());
                } finally {
                    semaphore.release();
                }
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error(Message.THREAD_INTERRUPTED, e.getMessage());
        } catch (Exception e) {
            log.error(Message.ERROR_PROCESSING_MESSAGE, topic, e.getMessage());
            semaphore.release();
            throw e; // Trigger retry mechanism
        }
    }

    @KafkaListener(topics = "#{@kafkaTopicConfig.getTopics()[1]}", groupId = "dltGroup")
    public void handleDlqMessage(String payload) {
        // Process or analyze the failed message
        log.error(Message.DLT_MESSAGE_RECEIVED, payload);
    }

    private EmailDTO getEmailMessageDTO(Long id) {
        EmailMessage emailEntity = emailMessageRepository.findById(id).orElseThrow();
        return EmailMessageMapper.from(emailEntity);
    }

    private void logMessageDetails(Long id, int partition, Long offset, String topic) {
        log.info(Message.PARTITION_DETAILS.formatted(id, partition, offset, topic));
    }

    @PreDestroy
    private void shutdown() {
        // Initiates an orderly shutdown in which previously submitted tasks are executed,
        // but no new tasks will be accepted.
        log.info(Message.SHUTTING_DOWN_EXECUTOR_SERVICE);
        executorService.shutdown();
        try {
            // Waits for up to 60 seconds for previously submitted tasks to complete execution.
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                // Forces the termination of all running tasks after the timeout.
                executorService.shutdownNow();
                // Waits again for up to 60 seconds for the forced termination to complete.
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    // Logs an error if the executor service did not terminate as expected.
                    log.error(Message.EXECUTOR_SERVICE_NOT_TERMINATED);
                }
            }
        } catch (InterruptedException e) {
            // If the current thread is interrupted while waiting, forcefully shuts down the executor.
            executorService.shutdownNow();
            // Restores the interrupted status of the current thread.
            Thread.currentThread().interrupt();
        }
    }
}
