package com.bannote.userservice.config;

import com.bannote.userservice.event.EventPublisher;
import com.google.protobuf.Message;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@Configuration
public class TestConfig {

    @MockitoBean(types = {EventPublisher.class})
    private KafkaTemplate<String, Message> kafkaTemplate;
}
