package com.bannote.userservice.event.user;

import com.bannote.commonservice.proto.events.v1.*;
import com.bannote.userservice.domain.user.UserDetail;
import com.google.protobuf.Timestamp;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * UserDetail 도메인을 Protobuf 이벤트로 변환하는 유틸리티 클래스
 */
public class UserEventBuilder {

    /**
     * User 생성 이벤트 빌드
     */
    public static UserChangedEvent buildUserCreatedEvent(UserDetail user, String triggeredBy) {
        com.bannote.userservice.proto.user.v1.UserDetail userProto = user.toProto();
        UserChangedEvent.Builder builder = UserChangedEvent.newBuilder()
                .setEventId(UUID.randomUUID().toString())
                .setEventType(EventType.EVENT_TYPE_CREATED)
                .setTimestamp(toProtoTimestamp(Instant.now()))
                .setTriggeredBy(triggeredBy)
                .setUserCode(userProto.getUserCode())
                .setUserEmail(userProto.getUserEmail())
                .setUserName(userProto.getUserName())
                .setFamilyName(userProto.getFamilyName())
                .setGivenName(userProto.getGivenName())
                .setUserType(convertProtoUserType(userProto.getUserType()))
                .setUserStatus(convertUserStatus(user.getUserBasic().getUserStatus()))
                .addAllUserRoles(userProto.getUserRolesList().stream()
                        .map(UserEventBuilder::convertProtoUserRole)
                        .collect(Collectors.toList()));

        // 학생인 경우 학반 정보
        if (userProto.hasStudentClassCode()) {
            builder.setStudentClassCode(userProto.getStudentClassCode());
        }
        if (userProto.hasStudentClassName()) {
            builder.setStudentClassName(userProto.getStudentClassName());
        }

        // 학과 정보 (학생/교직원)
        if (userProto.hasDepartmentCode()) {
            builder.setDepartmentCode(userProto.getDepartmentCode());
        }
        if (userProto.hasDepartmentName()) {
            builder.setDepartmentName(userProto.getDepartmentName());
        }

        return builder.build();
    }

    /**
     * User 업데이트 이벤트 빌드 (이전 데이터 없이)
     */
    public static UserChangedEvent buildUserUpdatedEvent(UserDetail user, String triggeredBy) {
        com.bannote.userservice.proto.user.v1.UserDetail userProto = user.toProto();
        UserChangedEvent.Builder builder = UserChangedEvent.newBuilder()
                .setEventId(UUID.randomUUID().toString())
                .setEventType(EventType.EVENT_TYPE_UPDATED)
                .setTimestamp(toProtoTimestamp(Instant.now()))
                .setTriggeredBy(triggeredBy)
                .setUserCode(userProto.getUserCode())
                .setUserEmail(userProto.getUserEmail())
                .setUserName(userProto.getUserName())
                .setFamilyName(userProto.getFamilyName())
                .setGivenName(userProto.getGivenName())
                .setUserType(convertProtoUserType(userProto.getUserType()))
                .setUserStatus(convertUserStatus(user.getUserBasic().getUserStatus()))
                .addAllUserRoles(userProto.getUserRolesList().stream()
                        .map(UserEventBuilder::convertProtoUserRole)
                        .collect(Collectors.toList()));

        // 학생인 경우 학반 정보
        if (userProto.hasStudentClassCode()) {
            builder.setStudentClassCode(userProto.getStudentClassCode());
        }
        if (userProto.hasStudentClassName()) {
            builder.setStudentClassName(userProto.getStudentClassName());
        }

        // 학과 정보 (학생/교직원)
        if (userProto.hasDepartmentCode()) {
            builder.setDepartmentCode(userProto.getDepartmentCode());
        }
        if (userProto.hasDepartmentName()) {
            builder.setDepartmentName(userProto.getDepartmentName());
        }

        return builder.build();
    }

    /**
     * User 삭제 이벤트 빌드
     */
    public static UserChangedEvent buildUserDeletedEvent(UserDetail user, String triggeredBy) {
        com.bannote.userservice.proto.user.v1.UserDetail userProto = user.toProto();
        UserChangedEvent.Builder builder = UserChangedEvent.newBuilder()
                .setEventId(UUID.randomUUID().toString())
                .setEventType(EventType.EVENT_TYPE_DELETED)
                .setTimestamp(toProtoTimestamp(Instant.now()))
                .setTriggeredBy(triggeredBy)
                .setUserCode(userProto.getUserCode())
                .setUserEmail(userProto.getUserEmail())
                .setUserName(userProto.getUserName())
                .setFamilyName(userProto.getFamilyName())
                .setGivenName(userProto.getGivenName())
                .setUserType(convertProtoUserType(userProto.getUserType()))
                .setUserStatus(convertUserStatus(user.getUserBasic().getUserStatus()));

        // 학생인 경우 학반 정보
        if (userProto.hasStudentClassCode()) {
            builder.setStudentClassCode(userProto.getStudentClassCode());
        }
        if (userProto.hasStudentClassName()) {
            builder.setStudentClassName(userProto.getStudentClassName());
        }

        // 학과 정보 (학생/교직원)
        if (userProto.hasDepartmentCode()) {
            builder.setDepartmentCode(userProto.getDepartmentCode());
        }
        if (userProto.hasDepartmentName()) {
            builder.setDepartmentName(userProto.getDepartmentName());
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
     * userservice의 UserStatus를 commonservice의 UserStatus로 변환
     */
    private static com.bannote.commonservice.proto.events.v1.UserStatus convertUserStatus(
            com.bannote.userservice.domain.user.field.UserStatus status) {
        return switch (status) {
            case PENDING -> com.bannote.commonservice.proto.events.v1.UserStatus.USER_STATUS_PENDING;
            case ACTIVE -> com.bannote.commonservice.proto.events.v1.UserStatus.USER_STATUS_ACTIVE;
            case GRADUATED -> com.bannote.commonservice.proto.events.v1.UserStatus.USER_STATUS_GRADUATED;
            case LEAVE -> com.bannote.commonservice.proto.events.v1.UserStatus.USER_STATUS_LEAVE;
            case SUSPENDED -> com.bannote.commonservice.proto.events.v1.UserStatus.USER_STATUS_SUSPENDED;
            case WITHDRAWN -> com.bannote.commonservice.proto.events.v1.UserStatus.USER_STATUS_WITHDRAWN;
            case EXPELLED -> com.bannote.commonservice.proto.events.v1.UserStatus.USER_STATUS_EXPELLED;
        };
    }

    /**
     * proto의 UserType을 commonservice의 UserType으로 변환
     */
    private static UserType convertProtoUserType(com.bannote.userservice.proto.common.v1.UserType protoUserType) {
        return switch (protoUserType) {
            case USER_TYPE_STUDENT -> UserType.USER_TYPE_STUDENT;
            case USER_TYPE_EMPLOYEE -> UserType.USER_TYPE_EMPLOYEE;
            case USER_TYPE_SERVICE -> UserType.USER_TYPE_SERVICE;
            case USER_TYPE_OTHER -> UserType.USER_TYPE_OTHER;
            default -> UserType.USER_TYPE_UNSPECIFIED;
        };
    }

    /**
     * proto의 UserRole을 commonservice의 UserRole로 변환
     */
    private static UserRole convertProtoUserRole(com.bannote.userservice.proto.common.v1.UserRole protoUserRole) {
        return switch (protoUserRole) {
            case USER_ROLE_STUDENT -> UserRole.USER_ROLE_STUDENT;
            case USER_ROLE_DOORKEEPER -> UserRole.USER_ROLE_DOORKEEPER;
            case USER_ROLE_CLASS_REP -> UserRole.USER_ROLE_CLASS_REP;
            case USER_ROLE_TA -> UserRole.USER_ROLE_TA;
            case USER_ROLE_PROFESSOR -> UserRole.USER_ROLE_PROFESSOR;
            case USER_ROLE_ADMIN -> UserRole.USER_ROLE_ADMIN;
            case USER_ROLE_DEFAULT -> UserRole.USER_ROLE_DEFAULT;
            default -> UserRole.USER_ROLE_UNSPECIFIED;
        };
    }
}