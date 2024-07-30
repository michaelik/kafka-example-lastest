package com.kafka.dtos;

import lombok.Builder;

@Builder
public record BookDTO(
        String title,
        String name
) {
}
