package com.bannote.userservice.domain.user.field;

import com.bannote.userservice.exception.ErrorCode;
import com.bannote.userservice.exception.UserServiceException;
import lombok.Getter;

import java.util.Objects;

public class UserFamilyName {

    private static final int MAX_LENGTH = 20;

    @Getter
    private final String value;

    private UserFamilyName(String value) {
        this.value = value;
    }

    public static UserFamilyName of(String value) {
        validate(value);
        return new UserFamilyName(value);
    }

    private static void validate(String value) {
        if (value == null || value.isEmpty()) {
            throw new UserServiceException(ErrorCode.REQUIRED_FIELD_MISSING, "User family name cannot be null or empty");
        }

        if (value.length() > MAX_LENGTH) {
            throw new UserServiceException(ErrorCode.INVALID_FORMAT,
                   String.format("User family name cannot be longer than %d characters: %s", MAX_LENGTH, value));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserFamilyName that = (UserFamilyName) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
