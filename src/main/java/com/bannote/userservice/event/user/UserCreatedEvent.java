package com.bannote.userservice.event.user;

import com.bannote.userservice.domain.user.UserDetail;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 유저 생성 도메인 이벤트
 * ApplicationService에서 발행하고, EventListener에서 Kafka로 전송
 */
@Getter
@RequiredArgsConstructor
public class UserCreatedEvent {
    private final UserDetail user;
    private final String triggeredBy;
}
