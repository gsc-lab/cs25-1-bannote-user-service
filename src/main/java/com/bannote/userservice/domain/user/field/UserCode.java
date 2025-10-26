package com.bannote.userservice.domain.user.field;

import com.bannote.userservice.exception.ErrorCode;
import com.bannote.userservice.exception.UserServiceException;
import lombok.Getter;

import java.util.Objects;

public class UserCode {

    private static final int CODE_LENGTH = 7;  // 유저 코드 길이

    @Getter
    private final String value;

    private UserCode(String value) {
        this.value = value;
    }

    public static UserCode of(String value) {
        validate(value);
        return new UserCode(value);
    }

    private static void validate(String value) {
        if (value == null || value.isEmpty()) {
            throw new UserServiceException(ErrorCode.REQUIRED_FIELD_MISSING, "UserCode is required");
        }

        if (value.length() != CODE_LENGTH) {
            throw new UserServiceException(ErrorCode.INVALID_FORMAT,
                    String.format("UserCode length must be %d characters: %s", CODE_LENGTH, value));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserCode userCode = (UserCode) o;
        return Objects.equals(value, userCode.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
