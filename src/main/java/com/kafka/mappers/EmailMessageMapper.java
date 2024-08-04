package com.kafka.mappers;

import com.kafka.dtos.EmailDTO;
import com.kafka.entity.EmailMessage;

public interface EmailMessageMapper {
    static EmailMessage to(EmailDTO request) {
        if (request == null) {
            return null;
        }
        return EmailMessage.builder()
                .from(request.from())
                .to(request.to())
                .subject(request.subject())
                .body(request.body())
                .build();
    }

    static EmailDTO from(EmailMessage request) {
        if (request == null) {
            return null;
        }
        return EmailDTO.builder()
                .from(request.getFrom())
                .to(request.getTo())
                .subject(request.getSubject())
                .body(request.getBody())
                .build();
    }

}
