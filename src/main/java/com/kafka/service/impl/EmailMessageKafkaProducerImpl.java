package com.kafka.service.impl;

import com.kafka.constant.Message;
import com.kafka.dtos.BookDTO;
import com.kafka.dtos.EmailDTO;
import com.kafka.entity.EmailMessage;
import com.kafka.mappers.EmailMessageMapper;
import com.kafka.repository.EmailMessageRepository;
import com.kafka.service.EmailMessageKafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class EmailMessageKafkaProducerImpl implements EmailMessageKafkaProducer {

    @Autowired
    private EmailMessageRepository repository;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Value("#{'${kafka.topics.user.name}'.split(',')}")
    private List<String> topics;

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

    @Override
    public void sendEmail(EmailDTO request) {
        EmailMessage emailEntity = create(request);
        sendToKafka(emailEntity.getId(), topics.get(0));
    }

    private EmailMessage create(EmailDTO request){
        EmailMessage emailEntity = EmailMessageMapper.to(request);
        return repository.save(emailEntity);
    }

    private void sendToKafka(Long request, String topic) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, request);

        future.thenAccept(sendResult -> handleSuccess(sendResult, request))
                .exceptionally(throwable -> handleFailure(throwable, request, topic));
    }

    private void handleSuccess(SendResult<String, Object> sendResult, Long request) {
        log.info(Message.PAYLOAD_PARTITION_OFFSET,
                request.toString(),
                sendResult.getRecordMetadata().toString());
    }

    private Void handleFailure(Throwable throwable, Long request, String topic) {
        log.error(Message.PAYLOAD_PARTITION_OFFSET,
                request.toString(),
                topic,
                throwable.toString());
        return null;
    }

}
