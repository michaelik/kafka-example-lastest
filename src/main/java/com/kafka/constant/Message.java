package com.kafka.constant;

public interface Message {
    String PARTITION_DETAILS = "Message received %s from Partition [%s] and offset [%s] from Topic [%s]";
    String ERROR_MESSAGE = "There was an error -> {}";
    String PAYLOAD_PARTITION_OFFSET = "Sent payload -> '{}' to topic-partition@offset -> '{}'";
    String ERROR_CREATING_TOPIC = "Kafka topics are not configured properly.";
    String ERROR_PROCESSING_MSG = "Error processing message from topic '{}': {}";
    String DLT_MESSAGE_RECEIVED = "Received message in DLT: {}";
}
