package com.bannote.userservice.event.studentclass;

import com.bannote.commonservice.proto.events.v1.EventType;
import com.bannote.commonservice.proto.events.v1.StudentClassChangedEvent;
import com.bannote.userservice.domain.studentclass.StudentClass;
import com.google.protobuf.Timestamp;

import java.time.Instant;
import java.util.UUID;

/**
 * StudentClass 도메인을 Protobuf 이벤트로 변환하는 유틸리티 클래스
 */
public class StudentClassEventBuilder {

    /**
     * StudentClass 생성 이벤트 빌드
     */
    public static StudentClassChangedEvent buildStudentClassCreatedEvent(StudentClass studentClass, String triggeredBy) {
        StudentClassChangedEvent.Builder builder = StudentClassChangedEvent.newBuilder()
                .setEventId(UUID.randomUUID().toString())
                .setEventType(EventType.EVENT_TYPE_CREATED)
                .setTimestamp(toProtoTimestamp(Instant.now()))
                .setTriggeredBy(triggeredBy)
                .setStudentClassCode(studentClass.getStudentClassCode().getValue())
                .setDepartmentCode(studentClass.getDepartment().getDepartmentCode().getValue())
                .setStudentClassName(studentClass.getStudentClassName().getValue())
                .setAdmissionYear(studentClass.getAdmissionYear().getValue())
                .setGraduationYear(studentClass.getGraduationYear().getValue())
                .setStatus(convertStudentClassStatus(studentClass.getStatus()))
                .setCreatedAt(toProtoTimestamp(studentClass.getCreatedAt().toInstant()))
                .setCreatedBy(studentClass.getCreatedBy());

        if (studentClass.getDeletedAt() != null) {
            builder.setDeletedAt(toProtoTimestamp(studentClass.getDeletedAt().toInstant()));
        }

        return builder.build();
    }

    /**
     * StudentClass 업데이트 이벤트 빌드
     */
    public static StudentClassChangedEvent buildStudentClassUpdatedEvent(StudentClass studentClass, String triggeredBy) {
        StudentClassChangedEvent.Builder builder = StudentClassChangedEvent.newBuilder()
                .setEventId(UUID.randomUUID().toString())
                .setEventType(EventType.EVENT_TYPE_UPDATED)
                .setTimestamp(toProtoTimestamp(Instant.now()))
                .setTriggeredBy(triggeredBy)
                .setStudentClassCode(studentClass.getStudentClassCode().getValue())
                .setDepartmentCode(studentClass.getDepartment().getDepartmentCode().getValue())
                .setStudentClassName(studentClass.getStudentClassName().getValue())
                .setAdmissionYear(studentClass.getAdmissionYear().getValue())
                .setGraduationYear(studentClass.getGraduationYear().getValue())
                .setStatus(convertStudentClassStatus(studentClass.getStatus()))
                .setCreatedAt(toProtoTimestamp(studentClass.getCreatedAt().toInstant()))
                .setCreatedBy(studentClass.getCreatedBy());

        if (studentClass.getDeletedAt() != null) {
            builder.setDeletedAt(toProtoTimestamp(studentClass.getDeletedAt().toInstant()));
        }

        return builder.build();
    }

    /**
     * StudentClass 삭제 이벤트 빌드
     */
    public static StudentClassChangedEvent buildStudentClassDeletedEvent(StudentClass studentClass, String triggeredBy) {
        StudentClassChangedEvent.Builder builder = StudentClassChangedEvent.newBuilder()
                .setEventId(UUID.randomUUID().toString())
                .setEventType(EventType.EVENT_TYPE_DELETED)
                .setTimestamp(toProtoTimestamp(Instant.now()))
                .setTriggeredBy(triggeredBy)
                .setStudentClassCode(studentClass.getStudentClassCode().getValue())
                .setDepartmentCode(studentClass.getDepartment().getDepartmentCode().getValue())
                .setStudentClassName(studentClass.getStudentClassName().getValue())
                .setAdmissionYear(studentClass.getAdmissionYear().getValue())
                .setGraduationYear(studentClass.getGraduationYear().getValue())
                .setStatus(convertStudentClassStatus(studentClass.getStatus()))
                .setCreatedAt(toProtoTimestamp(studentClass.getCreatedAt().toInstant()))
                .setCreatedBy(studentClass.getCreatedBy());

        if (studentClass.getDeletedAt() != null) {
            builder.setDeletedAt(toProtoTimestamp(studentClass.getDeletedAt().toInstant()));
        }

        return builder.build();
    }

    // === Conversion Helpers ===

    private static Timestamp toProtoTimestamp(Instant instant) {
        return Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
    }

    /**
     * StudentClassStatus를 proto StudentClassStatus로 변환
     */
    private static com.bannote.commonservice.proto.events.v1.StudentClassStatus convertStudentClassStatus(
            com.bannote.userservice.domain.studentclass.field.StudentClassStatus status) {
        return switch (status) {
            case ACTIVE -> com.bannote.commonservice.proto.events.v1.StudentClassStatus.STUDENT_CLASS_STATUS_ACTIVE;
            case GRADUATED -> com.bannote.commonservice.proto.events.v1.StudentClassStatus.STUDENT_CLASS_STATUS_GRADUATED;
        };
    }
}
