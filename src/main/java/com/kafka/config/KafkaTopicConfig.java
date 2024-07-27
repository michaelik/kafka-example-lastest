package com.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic topicOne() {
        return TopicBuilder.name("topicOne")
                .partitions(6)  // Number of partitions
                .replicas(1)     // Replication factor
                .config(TopicConfig.RETENTION_BYTES_CONFIG, "1073741824") // Retain up to 1 GB of messages
                .build();
    }

//    @Bean
//    public NewTopic topicTwo() {
//        return TopicBuilder.name("topicTwo")
//                .build();
//    }
}
