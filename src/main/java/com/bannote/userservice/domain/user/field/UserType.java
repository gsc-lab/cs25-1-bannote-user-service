package com.bannote.userservice.domain.user.field;

import com.bannote.userservice.exception.ErrorCode;
import com.bannote.userservice.exception.UserServiceException;
import lombok.Getter;

@Getter
public enum UserType {
    STUDENT("학생"),
    EMPLOYEE("직원"),
    SERVICE("서비스"),
    OTHER("기타"),
    ;

    private final String description;

    UserType(String description) {
        this.description = description;
    }

    public static UserType of(com.bannote.userservice.proto.common.v1.UserType proto) {
        return switch (proto) {
            case USER_TYPE_STUDENT -> STUDENT;
            case USER_TYPE_EMPLOYEE -> EMPLOYEE;
            case USER_TYPE_SERVICE -> SERVICE;
            case USER_TYPE_OTHER -> OTHER;
            default -> throw new UserServiceException(ErrorCode.INVALID_FORMAT,
                    "Invalid user type: " + proto);
        };
    }

    public com.bannote.userservice.proto.common.v1.UserType toProto() {
        return switch (this) {
            case STUDENT -> com.bannote.userservice.proto.common.v1.UserType.USER_TYPE_STUDENT;
            case EMPLOYEE -> com.bannote.userservice.proto.common.v1.UserType.USER_TYPE_EMPLOYEE;
            case SERVICE -> com.bannote.userservice.proto.common.v1.UserType.USER_TYPE_SERVICE;
            case OTHER -> com.bannote.userservice.proto.common.v1.UserType.USER_TYPE_OTHER;
        };
    }

    public Boolean isStudent() {
        return this == STUDENT;
    }

    public Boolean isEmployee() {
        return this == EMPLOYEE;
    }
}
