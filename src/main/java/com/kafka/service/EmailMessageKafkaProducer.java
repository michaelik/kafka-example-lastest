package com.kafka.service;

import com.kafka.dtos.BookDTO;
import com.kafka.dtos.EmailDTO;

public interface EmailMessageKafkaProducer {

    void sendMessage(BookDTO book);
    void sendEmail(EmailDTO request);
}
