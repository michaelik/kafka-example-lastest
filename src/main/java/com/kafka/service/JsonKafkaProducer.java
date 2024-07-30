package com.kafka.service;

import com.kafka.dtos.BookDTO;

public interface JsonKafkaProducer {

    void sendMessage(BookDTO book);
}
