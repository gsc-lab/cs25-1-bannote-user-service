package com.bannote.userservice.domain.department.field;

import com.bannote.userservice.exception.ErrorCode;
import com.bannote.userservice.exception.UserServiceException;
import lombok.Getter;

import java.util.Objects;

public class DepartmentCode {

    private static final int CODE_LENGTH = 7;  // 학과 코드 길이

    @Getter
    private final String value;

    private DepartmentCode(String value) {
        this.value = value;
    }

    public static DepartmentCode of(String value) {
        validate(value);
        return new DepartmentCode(value);
    }

    public static void validate(String value) {
        if (value == null || value.isEmpty()) {
            throw new UserServiceException(ErrorCode.REQUIRED_FIELD_MISSING, "DepartmentCode is required");
        }

        if (value.length() != CODE_LENGTH) {
            throw new UserServiceException(ErrorCode.INVALID_FORMAT,
                    String.format("DepartmentCode length must be %d characters: %s", CODE_LENGTH, value));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DepartmentCode userCode = (DepartmentCode) o;
        return Objects.equals(value, userCode.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
