package com.kafka.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kafka.dtos.Book;
import com.kafka.service.JsonKafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/kafka")
public class MessageController {

    @Autowired
    private JsonKafkaProducer jsonKafkaProducer;


    @PostMapping("/json-publish")
    public ResponseEntity<String> json_publish(
            @RequestBody Book book
    ) throws JsonProcessingException {
        jsonKafkaProducer.sendMessage(book);
        return ResponseEntity.ok("json message send to kafka topic");
    }
}
