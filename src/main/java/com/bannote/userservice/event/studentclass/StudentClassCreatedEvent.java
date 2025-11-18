package com.bannote.userservice.event.studentclass;

import com.bannote.userservice.domain.studentclass.StudentClass;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 학반 생성 도메인 이벤트
 * ApplicationService에서 발행하고, EventListener에서 Kafka로 전송
 */
@Getter
@RequiredArgsConstructor
public class StudentClassCreatedEvent {
    private final StudentClass studentClass;
    private final String triggeredBy;
}