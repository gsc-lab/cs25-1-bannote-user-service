package com.bannote.userservice.event.studentclass;

import com.bannote.commonservice.proto.events.v1.StudentClassChangedEvent;
import com.bannote.userservice.event.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * StudentClass 도메인 이벤트를 Kafka로 발행하는 리스너
 * 트랜잭션 커밋 후에 이벤트를 처리하여 데이터 일관성 보장
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StudentClassEventListener {

    private final EventPublisher eventPublisher;

    /**
     * 트랜잭션 커밋 후 StudentClass 생성 이벤트를 Kafka로 발행
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleStudentClassCreated(StudentClassCreatedEvent event) {
        log.info("Handling StudentClassCreatedEvent for studentClassCode: {}",
                event.getStudentClass().getStudentClassCode().getValue());

        StudentClassChangedEvent kafkaEvent = StudentClassEventBuilder.buildStudentClassCreatedEvent(
                event.getStudentClass(),
                event.getTriggeredBy()
        );

        eventPublisher.publishStudentClassChangedEvent(kafkaEvent)
                .exceptionally(ex -> {
                    log.error("Failed to publish StudentClassCreatedEvent to Kafka: studentClassCode={}",
                            event.getStudentClass().getStudentClassCode().getValue(), ex);
                    // TODO: 실패 시 재시도 로직 또는 Dead Letter Queue 구현
                    return null;
                });
    }

    /**
     * 트랜잭션 커밋 후 StudentClass 업데이트 이벤트를 Kafka로 발행
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleStudentClassUpdated(StudentClassUpdatedEvent event) {
        log.info("Handling StudentClassUpdatedEvent for studentClassCode: {}",
                event.getStudentClass().getStudentClassCode().getValue());

        StudentClassChangedEvent kafkaEvent = StudentClassEventBuilder.buildStudentClassUpdatedEvent(
                event.getStudentClass(),
                event.getTriggeredBy()
        );

        eventPublisher.publishStudentClassChangedEvent(kafkaEvent)
                .exceptionally(ex -> {
                    log.error("Failed to publish StudentClassUpdatedEvent to Kafka: studentClassCode={}",
                            event.getStudentClass().getStudentClassCode().getValue(), ex);
                    return null;
                });
    }

    /**
     * 트랜잭션 커밋 후 StudentClass 삭제 이벤트를 Kafka로 발행
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleStudentClassDeleted(StudentClassDeletedEvent event) {
        log.info("Handling StudentClassDeletedEvent for studentClassCode: {}",
                event.getStudentClass().getStudentClassCode().getValue());

        StudentClassChangedEvent kafkaEvent = StudentClassEventBuilder.buildStudentClassDeletedEvent(
                event.getStudentClass(),
                event.getTriggeredBy()
        );

        eventPublisher.publishStudentClassChangedEvent(kafkaEvent)
                .exceptionally(ex -> {
                    log.error("Failed to publish StudentClassDeletedEvent to Kafka: studentClassCode={}",
                            event.getStudentClass().getStudentClassCode().getValue(), ex);
                    return null;
                });
    }
}
