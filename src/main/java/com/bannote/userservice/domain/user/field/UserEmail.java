package com.bannote.userservice.domain.user.field;

import com.bannote.userservice.exception.ErrorCode;
import com.bannote.userservice.exception.UserServiceException;
import lombok.Getter;

import java.util.Objects;

public class UserEmail {

    @Getter
    private final String value;

    private UserEmail(String value) {
        this.value = value;
    }

    public static UserEmail of(String value) {
        validate(value);
        return new UserEmail(value);
    }

    private static void validate(String value) {
        if (value == null || value.isEmpty()) {
            throw new UserServiceException(ErrorCode.REQUIRED_FIELD_MISSING, "UserEmail is required");
        }

        if (!value.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new UserServiceException(ErrorCode.INVALID_FORMAT,
                    String.format("UserEmail format is invalid: %s", value));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserEmail userEmail = (UserEmail) o;
        return Objects.equals(value, userEmail.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
