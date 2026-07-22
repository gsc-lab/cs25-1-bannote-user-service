package com.bannote.userservice.domain.user.field;

import com.bannote.userservice.exception.ErrorCode;
import com.bannote.userservice.exception.UserServiceException;
import lombok.Getter;

// TODO: 서비스가 커지게 될 경우 권한 서비스로 분리 예정

@Getter
public enum UserRole {
    DEFAULT(0, "Default"),
    STUDENT(10, "Student"),                    // 학생
    DOORKEEPER(20, "Doorkeeper"),              // 키지기
    CLASS_REP(30, "Class Representative"),     // 반대표
    TA(40, "Teaching Assistant"),              // 조교
    PROFESSOR(50, "Professor"),                // 교수
    ADMIN(60, "Admin");                        // 관리자

    private final int level;
    private final String name;

    UserRole(int level, String name) {
        this.level = level;
        this.name = name;
    }

    /**
     * 요구되는 권한 레벨을 충족하는지 확인
     */
    public boolean hasAuthority(int requiredLevel) {
        return this.level >= requiredLevel;
    }

    public String toString() {
        return this.name;
    }

    public static UserRole of(com.bannote.userservice.proto.common.v1.UserRole proto) {
        return switch (proto) {
            case USER_ROLE_DEFAULT -> DEFAULT;
            case USER_ROLE_STUDENT -> STUDENT;
            case USER_ROLE_DOORKEEPER -> DOORKEEPER;
            case USER_ROLE_CLASS_REP -> CLASS_REP;
            case USER_ROLE_TA -> TA;
            case USER_ROLE_PROFESSOR -> PROFESSOR;
            case USER_ROLE_ADMIN -> ADMIN;
            default -> throw new UserServiceException(ErrorCode.INVALID_FORMAT,
                    "Invalid user role: " + proto);
        };
    }

    public com.bannote.userservice.proto.common.v1.UserRole toProto() {
        return switch (this) {
            case DEFAULT -> com.bannote.userservice.proto.common.v1.UserRole.USER_ROLE_DEFAULT;
            case STUDENT -> com.bannote.userservice.proto.common.v1.UserRole.USER_ROLE_STUDENT;
            case DOORKEEPER -> com.bannote.userservice.proto.common.v1.UserRole.USER_ROLE_DOORKEEPER;
            case CLASS_REP -> com.bannote.userservice.proto.common.v1.UserRole.USER_ROLE_CLASS_REP;
            case TA -> com.bannote.userservice.proto.common.v1.UserRole.USER_ROLE_TA;
            case PROFESSOR -> com.bannote.userservice.proto.common.v1.UserRole.USER_ROLE_PROFESSOR;
            case ADMIN -> com.bannote.userservice.proto.common.v1.UserRole.USER_ROLE_ADMIN;
        };
    }
}
