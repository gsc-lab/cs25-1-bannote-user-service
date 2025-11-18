package com.bannote.userservice.event;

import com.bannote.commonservice.proto.events.v1.DepartmentChangedEvent;
import com.bannote.commonservice.proto.events.v1.StudentClassChangedEvent;
import com.bannote.commonservice.proto.events.v1.UserChangedEvent;
import com.google.protobuf.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class EventPublisher {

    private final KafkaTemplate<String, Message> kafkaTemplate;

    @Value("${spring.kafka.topics.user}")
    private String userChangedTopic;

    @Value("${spring.kafka.topics.department}")
    private String departmentChangedTopic;

    @Value("${spring.kafka.topics.student-class}")
    private String studentClassChangedTopic;

    public EventPublisher(KafkaTemplate<String, Message> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * 유저 변경 이벤트 발행
     *
     * @param event 유저 변경 이벤트
     * @return CompletableFuture<SendResult>
     */
    public CompletableFuture<SendResult<String, Message>> publishUserChangedEvent(UserChangedEvent event) {
        String key = event.getUserCode();
        log.info("Publishing UserChangedEvent: eventId={}, userCode={}, eventType={}",
                event.getEventId(), event.getUserCode(), event.getEventType());

        return kafkaTemplate.send(userChangedTopic, key, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish UserChangedEvent: eventId={}, error={}",
                                event.getEventId(), ex.getMessage(), ex);
                    } else {
                        log.info("Successfully published UserChangedEvent: eventId={}, partition={}, offset={}",
                                event.getEventId(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }

    /**
     * 학과 변경 이벤트 발행
     *
     * @param event 학과 변경 이벤트
     * @return CompletableFuture<SendResult>
     */
    public CompletableFuture<SendResult<String, Message>> publishDepartmentChangedEvent(DepartmentChangedEvent event) {
        String key = event.getDepartmentCode();
        log.info("Publishing DepartmentChangedEvent: eventId={}, departmentCode={}, eventType={}",
                event.getEventId(), event.getDepartmentCode(), event.getEventType());

        return kafkaTemplate.send(departmentChangedTopic, key, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish DepartmentChangedEvent: eventId={}, error={}",
                                event.getEventId(), ex.getMessage(), ex);
                    } else {
                        log.info("Successfully published DepartmentChangedEvent: eventId={}, partition={}, offset={}",
                                event.getEventId(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }

    /**
     * 학반 변경 이벤트 발행
     *
     * @param event 학반 변경 이벤트
     * @return CompletableFuture<SendResult>
     */
    public CompletableFuture<SendResult<String, Message>> publishStudentClassChangedEvent(StudentClassChangedEvent event) {
        String key = event.getStudentClassCode();
        log.info("Publishing StudentClassChangedEvent: eventId={}, studentClassCode={}, eventType={}",
                event.getEventId(), event.getStudentClassCode(), event.getEventType());

        return kafkaTemplate.send(studentClassChangedTopic, key, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish StudentClassChangedEvent: eventId={}, error={}",
                                event.getEventId(), ex.getMessage(), ex);
                    } else {
                        log.info("Successfully published StudentClassChangedEvent: eventId={}, partition={}, offset={}",
                                event.getEventId(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }
}
