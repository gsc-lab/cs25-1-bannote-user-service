package com.bannote.userservice.domain.studentclass.field;

import com.bannote.userservice.exception.ErrorCode;
import com.bannote.userservice.exception.UserServiceException;

public enum StudentClassStatus {
    ACTIVE, GRADUATED
    ;

    public static StudentClassStatus of(com.bannote.userservice.proto.common.v1.StudentClassStatus proto) {
        return switch (proto) {
            case STUDENT_CLASS_STATUS_ACTIVE -> ACTIVE;
            case STUDENT_CLASS_STATUS_GRADUATED -> GRADUATED;
            default -> throw new UserServiceException(ErrorCode.INVALID_FORMAT,
                    "Invalid Student class: " + proto);
        };
    }

    public com.bannote.userservice.proto.common.v1.StudentClassStatus toProto() {
        return switch (this) {
            case ACTIVE -> com.bannote.userservice.proto.common.v1.StudentClassStatus.STUDENT_CLASS_STATUS_ACTIVE;
            case GRADUATED -> com.bannote.userservice.proto.common.v1.StudentClassStatus.STUDENT_CLASS_STATUS_GRADUATED;
        };
    }
}
