package com.bannote.userservice.domain.studentclass.field;

public enum StudentClassStatus {
    ACTIVE, GRADUATED
    ;

    public com.bannote.userservice.proto.common.v1.StudentClassStatus toProto() {
        return switch (this) {
            case ACTIVE -> com.bannote.userservice.proto.common.v1.StudentClassStatus.STUDENT_CLASS_STATUS_ACTIVE;
            case GRADUATED -> com.bannote.userservice.proto.common.v1.StudentClassStatus.STUDENT_CLASS_STATUS_GRADUATED;
        };
    }
}
