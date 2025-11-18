package com.bannote.userservice.event.department;

import com.bannote.commonservice.proto.events.v1.DepartmentChangedEvent;
import com.bannote.commonservice.proto.events.v1.EventType;
import com.bannote.userservice.domain.department.Department;
import com.google.protobuf.Timestamp;

import java.time.Instant;
import java.util.UUID;

/**
 * Department 도메인을 Protobuf 이벤트로 변환하는 유틸리티 클래스
 */
public class DepartmentEventBuilder {

    /**
     * Department 생성 이벤트 빌드
     */
    public static DepartmentChangedEvent buildDepartmentCreatedEvent(Department department, String triggeredBy) {
        return DepartmentChangedEvent.newBuilder()
                .setEventId(UUID.randomUUID().toString())
                .setEventType(EventType.EVENT_TYPE_CREATED)
                .setTimestamp(toProtoTimestamp(Instant.now()))
                .setTriggeredBy(triggeredBy)
                .setDepartmentCode(department.getDepartmentCode().getValue())
                .setDepartmentName(department.getDepartmentName().getValue())
                .setCreatedAt(toProtoTimestamp(department.getCreatedAt().toInstant()))
                .build();
    }

    /**
     * Department 업데이트 이벤트 빌드
     */
    public static DepartmentChangedEvent buildDepartmentUpdatedEvent(Department department, String triggeredBy) {
        return DepartmentChangedEvent.newBuilder()
                .setEventId(UUID.randomUUID().toString())
                .setEventType(EventType.EVENT_TYPE_UPDATED)
                .setTimestamp(toProtoTimestamp(Instant.now()))
                .setTriggeredBy(triggeredBy)
                .setDepartmentCode(department.getDepartmentCode().getValue())
                .setDepartmentName(department.getDepartmentName().getValue())
                .setCreatedAt(toProtoTimestamp(department.getCreatedAt().toInstant()))
                .build();
    }

    /**
     * Department 삭제 이벤트 빌드
     */
    public static DepartmentChangedEvent buildDepartmentDeletedEvent(Department department, String triggeredBy) {
        return DepartmentChangedEvent.newBuilder()
                .setEventId(UUID.randomUUID().toString())
                .setEventType(EventType.EVENT_TYPE_DELETED)
                .setTimestamp(toProtoTimestamp(Instant.now()))
                .setTriggeredBy(triggeredBy)
                .setDepartmentCode(department.getDepartmentCode().getValue())
                .setDepartmentName(department.getDepartmentName().getValue())
                .setCreatedAt(toProtoTimestamp(department.getCreatedAt().toInstant()))
                .build();
    }

    // === Conversion Helpers ===

    private static Timestamp toProtoTimestamp(Instant instant) {
        return Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
    }
}