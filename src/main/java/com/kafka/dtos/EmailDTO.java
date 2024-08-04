package com.kafka.dtos;

import lombok.Builder;

import java.util.List;

@Builder
public record EmailDTO(
        String from,
        List<String> to,
        String subject,
        String body
) {
}
