package com.bannote.userservice.event.user;

import com.bannote.commonservice.proto.events.v1.UserChangedEvent;
import com.bannote.userservice.event.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * User 도메인 이벤트를 Kafka로 발행하는 리스너
 * 트랜잭션 커밋 후에 이벤트를 처리하여 데이터 일관성 보장
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventListener {

    private final EventPublisher eventPublisher;

    /**
     * 트랜잭션 커밋 후 User 생성 이벤트를 Kafka로 발행
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserCreated(UserCreatedEvent event) {
        log.info("Handling UserCreatedEvent for userCode: {}", event.getUser().getUserBasic().getUserCode());

        UserChangedEvent kafkaEvent = UserEventBuilder.buildUserCreatedEvent(
                event.getUser(),
                event.getTriggeredBy()
        );

        eventPublisher.publishUserChangedEvent(kafkaEvent)
                .exceptionally(ex -> {
                    log.error("Failed to publish UserCreatedEvent to Kafka: userCode={}",
                            event.getUser().getUserBasic().getUserCode(), ex);
                    // TODO: 실패 시 재시도 로직 또는 Dead Letter Queue 구현
                    return null;
                });
    }

    /**
     * 트랜잭션 커밋 후 User 업데이트 이벤트를 Kafka로 발행
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserUpdated(UserUpdatedEvent event) {
        log.info("Handling UserUpdatedEvent for userCode: {}", event.getUser().getUserBasic().getUserCode());

        UserChangedEvent kafkaEvent = UserEventBuilder.buildUserUpdatedEvent(
                event.getUser(),
                event.getTriggeredBy()
        );

        eventPublisher.publishUserChangedEvent(kafkaEvent)
                .exceptionally(ex -> {
                    log.error("Failed to publish UserUpdatedEvent to Kafka: userCode={}",
                            event.getUser().getUserBasic().getUserCode(), ex);
                    return null;
                });
    }

    /**
     * 트랜잭션 커밋 후 User 삭제 이벤트를 Kafka로 발행
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserDeleted(UserDeletedEvent event) {
        log.info("Handling UserDeletedEvent for userCode: {}", event.getUser().getUserBasic().getUserCode());

        UserChangedEvent kafkaEvent = UserEventBuilder.buildUserDeletedEvent(
                event.getUser(),
                event.getTriggeredBy()
        );

        eventPublisher.publishUserChangedEvent(kafkaEvent)
                .exceptionally(ex -> {
                    log.error("Failed to publish UserDeletedEvent to Kafka: userCode={}",
                            event.getUser().getUserBasic().getUserCode(), ex);
                    return null;
                });
    }
}