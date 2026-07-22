package com.bannote.userservice.domain.user.field;

import com.bannote.userservice.exception.ErrorCode;
import com.bannote.userservice.exception.UserServiceException;

import java.util.Objects;

/**
 * 사용자 프로필 URL을 나타내는 값 객체<br>
 * - 비어있을 경우 기본 프로필 이미지 사용<br>
 * - "http"로 시작하는 경우 절대 URL로 간주<br>
 * - "/"로 시작하는 경우 도메인과 결합하여 절대 URL로 변환
 */
public class UserProfileImage {

//    TODO: 실제 서버 도메인으로 변경 및 환경 변수 처리
    private static final String DOMAIN = "http://localhost:8080";
    private static final String DEFAULT_PROFILE = "/images/default-profile.png";

    private final String value;

    private UserProfileImage(String value) {
        this.value = value;
    }

    public static UserProfileImage of(String value) {
        validate(value);
        return new UserProfileImage(value);
    }

    private static void validate(String value) {
        if (value == null || value.isEmpty()) {
            return;
        }

        if (!value.startsWith("http") && !value.startsWith("/")) {
            throw new UserServiceException(ErrorCode.INVALID_FORMAT,
                    String.format("User profile must start with 'http' or '/': %s", value));
        }
    }

    public String getValue() {
        if (value.startsWith("http")) {
            return value;
        } else if (value.startsWith("/")) {
            return DOMAIN + value;
        } else {
            return DOMAIN + DEFAULT_PROFILE;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserProfileImage that = (UserProfileImage) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
