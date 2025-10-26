package com.bannote.userservice.domain.studentclass.field;

import com.bannote.userservice.exception.ErrorCode;
import com.bannote.userservice.exception.UserServiceException;
import lombok.Getter;

import java.util.Objects;

public class StudentClassCode {

    private static final int CODE_LENGTH = 7;  // 반 코드 길이

    @Getter
    private final String value;

    private StudentClassCode(String value) {
        this.value = value;
    }

    public static StudentClassCode of(String value) {
        validate(value);
        return new StudentClassCode(value);
    }

    private static void validate(String value) {
        if (value == null || value.isEmpty()) {
            throw new UserServiceException(ErrorCode.REQUIRED_FIELD_MISSING, "StudentClassCode is required");
        }

        if (value.length() != CODE_LENGTH) {
            throw new UserServiceException(ErrorCode.INVALID_FORMAT,
                    String.format("StudentClassCode length must be %d characters: %s", CODE_LENGTH, value));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StudentClassCode userCode = (StudentClassCode) o;
        return Objects.equals(value, userCode.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
