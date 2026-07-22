package com.bannote.userservice.domain.user.field;

import com.bannote.userservice.exception.ErrorCode;
import com.bannote.userservice.exception.UserServiceException;
import lombok.Getter;

import java.util.Objects;

public class UserGivenName {

    private static final int MAX_LENGTH = 20;

    @Getter
    private final String value;

    private UserGivenName(String value) {
        this.value = value;
    }

    public static UserGivenName of(String value) {
        validate(value);
        return new UserGivenName(value);
    }

    private static void validate(String value) {
        if (value == null || value.isEmpty()) {
            throw new UserServiceException(ErrorCode.REQUIRED_FIELD_MISSING, "User given name cannot be null or empty");
        }

        if (value.length() > MAX_LENGTH) {
            throw new UserServiceException(ErrorCode.INVALID_FORMAT,
                   String.format("User given name cannot be longer than %d characters: %s", MAX_LENGTH, value));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserGivenName that = (UserGivenName) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
