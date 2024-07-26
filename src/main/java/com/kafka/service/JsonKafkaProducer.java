package com.kafka.service;

import com.kafka.dtos.Book;

public interface JsonKafkaProducer {

    void sendMessage(Book book);
}
