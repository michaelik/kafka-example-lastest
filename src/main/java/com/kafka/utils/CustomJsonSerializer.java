package com.kafka.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class CustomJsonSerializer<T> extends JsonSerializer<T> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, T data) {
        if (data == null) {
            return null;
        }

        try {
            ObjectNode node = objectMapper.createObjectNode();
            node.put("@class", data.getClass().getName());
            node.setAll((ObjectNode) objectMapper.valueToTree(data));
            return node.toString().getBytes();
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize object", e);
        }
    }
}
