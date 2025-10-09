package com.bannote.userservice.domain.user.field;

import com.bannote.userservice.exception.ErrorCode;
import com.bannote.userservice.exception.UserServiceException;
import lombok.Getter;

@Getter
public enum UserStatus {
    PENDING("가입 대기"),
    ACTIVE("활성"),
    GRADUATED("졸업"),
    LEAVE("휴학"),
    SUSPENDED("정학"),
    WITHDRAWN("자퇴"),
    EXPELLED("제적")
    ;

    private final String description;

    UserStatus(String description) {
        this.description = description;

    }

    public UserStatus of(String value) {
        return switch (value.toUpperCase()) {
            case "STUDENT" -> PENDING;
            case "EMPLOYEE" -> ACTIVE;
            default -> throw new UserServiceException(ErrorCode.INVALID_FORMAT,
                    "Invalid user type: " + value);
        };
    }

    public com.bannote.userservice.proto.common.v1.UserStatus toProto() {
        return switch (this) {
            case PENDING -> com.bannote.userservice.proto.common.v1.UserStatus.USER_STATUS_PENDING;
            case ACTIVE -> com.bannote.userservice.proto.common.v1.UserStatus.USER_STATUS_ACTIVE;
            case GRADUATED -> com.bannote.userservice.proto.common.v1.UserStatus.USER_STATUS_GRADUATED;
            case LEAVE -> com.bannote.userservice.proto.common.v1.UserStatus.USER_STATUS_LEAVE;
            case SUSPENDED -> com.bannote.userservice.proto.common.v1.UserStatus.USER_STATUS_SUSPENDED;
            case WITHDRAWN -> com.bannote.userservice.proto.common.v1.UserStatus.USER_STATUS_WITHDRAWN;
            case EXPELLED -> com.bannote.userservice.proto.common.v1.UserStatus.USER_STATUS_EXPELLED;
        };
    }

    /**
     * 로그인 가능한 상태인지 확인
     * @param status 유저 상태
     * @return 로그인 가능 여부
     */
    public static Boolean LoginAllowed(UserStatus status) {
        return status == ACTIVE || status == GRADUATED || status == LEAVE;
    }
}
