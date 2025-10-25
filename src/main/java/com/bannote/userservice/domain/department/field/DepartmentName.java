package com.bannote.userservice.domain.department.field;

import com.bannote.userservice.exception.ErrorCode;
import com.bannote.userservice.exception.UserServiceException;
import lombok.Getter;

import java.util.Objects;

public class DepartmentName {

    private static final int MXA_LENGTH = 20;  // 학과 이름 최대 길이

    @Getter
    private final String value;

    private DepartmentName(String value) {
        this.value = value;
    }

    public static DepartmentName of(String value) {
        validate(value);
        return new DepartmentName(value);
    }

    private static void validate(String value) {
        if (value == null || value.isEmpty()) {
            throw new UserServiceException(ErrorCode.REQUIRED_FIELD_MISSING, "DepartmentName is required");
        }

        if (value.length() > MXA_LENGTH) {
            throw new UserServiceException(ErrorCode.INVALID_FORMAT,
                    String.format("DepartmentName cannot be longer than %d characters: %s", MXA_LENGTH, value));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DepartmentName userCode = (DepartmentName) o;
        return Objects.equals(value, userCode.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
