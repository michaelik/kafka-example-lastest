package com.kafka.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kafka.dtos.BookDTO;
import com.kafka.dtos.EmailDTO;
import com.kafka.service.EmailMessageKafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/email")
public class MessageController {

    @Autowired
    private EmailMessageKafkaProducer emailMessageKafkaProducer;


    @PostMapping("/json-publish")
    public ResponseEntity<String> json_publish(
            @RequestBody BookDTO book
    ) throws JsonProcessingException {
        emailMessageKafkaProducer.sendMessage(book);
        return ResponseEntity.ok("json message send to kafka topic");
    }

    @PostMapping("/messages")
    public void create(@RequestBody EmailDTO request) {
        // logic for publishing email to kafka
        emailMessageKafkaProducer.sendEmail(request);
    }
}
