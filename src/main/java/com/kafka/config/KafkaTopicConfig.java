package com.kafka.config;

import com.kafka.constant.Message;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.List;

@Configuration
public class KafkaTopicConfig {

    @Value("#{'${kafka.topics.user.name}'.split(',')}")
    private List<String> topics;


    @Bean
    public NewTopic topicOne() {
        if (topics == null || topics.isEmpty()) {
            throw new IllegalArgumentException(Message.ERROR_CREATING_TOPIC);
        }
        return TopicBuilder.name(topics.get(0))
                .partitions(3)  // Number of partitions
                .replicas(1)     // Replication factor
                .config(TopicConfig.RETENTION_BYTES_CONFIG, "1073741824") // Retain up to 1 GB of messages
                .build();
    }

    @Bean
    public NewTopic topicOneDlt() {
        if (topics == null || topics.isEmpty()) {
            throw new IllegalArgumentException(Message.ERROR_CREATING_TOPIC);
        }
        return TopicBuilder.name(topics.get(1))
                .config(TopicConfig.RETENTION_BYTES_CONFIG, "1073741824")
                .build();
    }
}
