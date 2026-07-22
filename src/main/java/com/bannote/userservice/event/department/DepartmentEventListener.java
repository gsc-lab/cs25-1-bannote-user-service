package com.bannote.userservice.event.department;

import com.bannote.commonservice.proto.events.v1.DepartmentChangedEvent;
import com.bannote.userservice.event.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Department 도메인 이벤트를 Kafka로 발행하는 리스너
 * 트랜잭션 커밋 후에 이벤트를 처리하여 데이터 일관성 보장
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DepartmentEventListener {

    private final EventPublisher eventPublisher;

    /**
     * 트랜잭션 커밋 후 Department 생성 이벤트를 Kafka로 발행
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleDepartmentCreated(DepartmentCreatedEvent event) {
        log.info("Handling DepartmentCreatedEvent for departmentCode: {}",
                event.getDepartment().getDepartmentCode().getValue());

        DepartmentChangedEvent kafkaEvent = DepartmentEventBuilder.buildDepartmentCreatedEvent(
                event.getDepartment(),
                event.getTriggeredBy()
        );

        eventPublisher.publishDepartmentChangedEvent(kafkaEvent)
                .exceptionally(ex -> {
                    log.error("Failed to publish DepartmentCreatedEvent to Kafka: departmentCode={}",
                            event.getDepartment().getDepartmentCode().getValue(), ex);
                    // TODO: 실패 시 재시도 로직 또는 Dead Letter Queue 구현
                    return null;
                });
    }

    /**
     * 트랜잭션 커밋 후 Department 업데이트 이벤트를 Kafka로 발행
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleDepartmentUpdated(DepartmentUpdatedEvent event) {
        log.info("Handling DepartmentUpdatedEvent for departmentCode: {}",
                event.getDepartment().getDepartmentCode().getValue());

        DepartmentChangedEvent kafkaEvent = DepartmentEventBuilder.buildDepartmentUpdatedEvent(
                event.getDepartment(),
                event.getTriggeredBy()
        );

        eventPublisher.publishDepartmentChangedEvent(kafkaEvent)
                .exceptionally(ex -> {
                    log.error("Failed to publish DepartmentUpdatedEvent to Kafka: departmentCode={}",
                            event.getDepartment().getDepartmentCode().getValue(), ex);
                    return null;
                });
    }

    /**
     * 트랜잭션 커밋 후 Department 삭제 이벤트를 Kafka로 발행
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleDepartmentDeleted(DepartmentDeletedEvent event) {
        log.info("Handling DepartmentDeletedEvent for departmentCode: {}",
                event.getDepartment().getDepartmentCode().getValue());

        DepartmentChangedEvent kafkaEvent = DepartmentEventBuilder.buildDepartmentDeletedEvent(
                event.getDepartment(),
                event.getTriggeredBy()
        );

        eventPublisher.publishDepartmentChangedEvent(kafkaEvent)
                .exceptionally(ex -> {
                    log.error("Failed to publish DepartmentDeletedEvent to Kafka: departmentCode={}",
                            event.getDepartment().getDepartmentCode().getValue(), ex);
                    return null;
                });
    }
}