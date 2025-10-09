package com.bannote.userservice.domain.user.field;

import com.bannote.userservice.exception.ErrorCode;
import com.bannote.userservice.exception.UserServiceException;
import lombok.Getter;

@Getter
public enum UserType {
    STUDENT("학생"),
    EMPLOYEE("직원")
    ;

    private final String description;

    UserType(String description) {
        this.description = description;
    }

    public UserType of(String value) {
        return switch (value.toUpperCase()) {
            case "STUDENT" -> STUDENT;
            case "EMPLOYEE" -> EMPLOYEE;
            default -> throw new UserServiceException(ErrorCode.INVALID_FORMAT,
                    "Invalid user type: " + value);
        };
    }

    public com.bannote.userservice.proto.common.v1.UserType toProto() {
        return switch (this) {
            case STUDENT -> com.bannote.userservice.proto.common.v1.UserType.USER_TYPE_STUDENT;
            case EMPLOYEE -> com.bannote.userservice.proto.common.v1.UserType.USER_TYPE_EMPLOYEE;
        };
    }

    public Boolean isStudent() {
        return this == STUDENT;
    }

    public Boolean isEmployee() {
        return this == EMPLOYEE;
    }
}
