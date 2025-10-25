package com.bannote.userservice.domain.user.field;

import com.bannote.userservice.exception.ErrorCode;
import com.bannote.userservice.exception.UserServiceException;
import lombok.Getter;

import java.util.Objects;

@Getter
public class UserBio {

    private static final int MAX_LENGTH = 300;

    @Getter
    private final String value;

    private UserBio(String value) {
        this.value = value;
    }

    public static UserBio of(String value) {
        validate(value);
        return new UserBio(value);
    }

    public static UserBio empty() {
        return new UserBio("");
    }

    private static void validate(String value) {
        // Bio는 빈 문자열 허용
        if (value == null) {
            throw new UserServiceException(ErrorCode.REQUIRED_FIELD_MISSING, "User Bio cannot be null or empty");
        }

        if (value.length() > MAX_LENGTH) {
            throw new UserServiceException(ErrorCode.INVALID_FORMAT,
                   String.format("User Bio cannot be longer than %d characters: %s", MAX_LENGTH, value));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserBio that = (UserBio) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
