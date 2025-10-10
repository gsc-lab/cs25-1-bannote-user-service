package com.bannote.userservice.domain.studentclass.field;

import com.bannote.userservice.exception.ErrorCode;
import com.bannote.userservice.exception.UserServiceException;
import lombok.Getter;

import java.util.Objects;

public class StudentClassName {

    private static final int MAX_LENGTH = 20;  // 학과 이름 최대 길이

    @Getter
    private final String value;

    private StudentClassName(String value) {
        this.value = value;
    }

    public static StudentClassName of(String value) {
        validate(value);
        return new StudentClassName(value);
    }

    private static void validate(String value) {
        if (value == null || value.isEmpty()) {
            throw new UserServiceException(ErrorCode.REQUIRED_FIELD_MISSING, "DepartmentName is required");
        }

        if (value.length() != MAX_LENGTH) {
            throw new UserServiceException(ErrorCode.INVALID_FORMAT,
                    String.format("DepartmentName cannot be longer than %d characters: %s", MAX_LENGTH, value));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StudentClassName userCode = (StudentClassName) o;
        return Objects.equals(value, userCode.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
