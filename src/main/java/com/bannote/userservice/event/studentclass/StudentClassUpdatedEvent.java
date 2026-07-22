package com.bannote.userservice.event.studentclass;

import com.bannote.userservice.domain.studentclass.StudentClass;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 학반 업데이트 도메인 이벤트
 * ApplicationService에서 발행하고, EventListener에서 Kafka로 전송
 */
@Getter
@RequiredArgsConstructor
public class StudentClassUpdatedEvent {
    private final StudentClass studentClass;
    private final String triggeredBy;
}