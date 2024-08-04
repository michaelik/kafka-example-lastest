package com.kafka.service;

import com.kafka.dtos.EmailDTO;

public interface EmailMessageSenderService {
    void sendEmail(EmailDTO request);
}
