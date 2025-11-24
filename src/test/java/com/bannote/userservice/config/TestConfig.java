package com.bannote.userservice.config;

import com.google.protobuf.Message;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.mock;

@Profile("test")
@Configuration
public class TestConfig {

    @Bean
    public KafkaTemplate<String, Message> kafkaTemplate() {
        return mock(KafkaTemplate.class);
    }
}
