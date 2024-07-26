package com.kafka.dtos;

import lombok.Builder;

@Builder
public record Book(
        String title,
        String name
) {
}
