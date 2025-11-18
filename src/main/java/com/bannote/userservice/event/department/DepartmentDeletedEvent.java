package com.bannote.userservice.event.department;

import com.bannote.userservice.domain.department.Department;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 학과 삭제 도메인 이벤트
 * ApplicationService에서 발행하고, EventListener에서 Kafka로 전송
 */
@Getter
@RequiredArgsConstructor
public class DepartmentDeletedEvent {
    private final Department department;
    private final String triggeredBy;
}