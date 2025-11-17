package com.bannote.userservice.domain.allowedDomain.field;

import com.bannote.userservice.exception.ErrorCode;
import com.bannote.userservice.exception.UserServiceException;
import lombok.Getter;

import java.util.Objects;

public class Domain {

    @Getter
    private final String value;

    private Domain(String value) {
        this.value = value;
    }

    public static Domain of(String value) {
        validate(value);
        return new Domain(value);
    }

    private static void validate(String value) {
        if (value == null || value.isEmpty()) {
            throw new UserServiceException(ErrorCode.REQUIRED_FIELD_MISSING, "Domain is required");
        }

        // 점으로 구분된 레이블들로 분리
        String[] labels = value.split("\\.");

        // 최소 2개 이상의 레이블이 있어야 함 (예: example.com)
        if (labels.length < 2) {
            throw new UserServiceException(ErrorCode.INVALID_FORMAT,
                    String.format("Domain must have at least two labels separated by dots: %s", value));
        }

        // 각 레이블 검증
        for (String label : labels) {
            validateLabel(label, value);
        }

        // 추가 검증: 점으로 시작하거나 끝나는 경우
        if (value.startsWith(".") || value.endsWith(".")) {
            throw new UserServiceException(ErrorCode.INVALID_FORMAT,
                    String.format("Domain cannot start or end with a dot: %s", value));
        }

        // 추가 검증: 연속된 점이 있는 경우
        if (value.contains("..")) {
            throw new UserServiceException(ErrorCode.INVALID_FORMAT,
                    String.format("Domain cannot contain consecutive dots: %s", value));
        }
    }

    private static void validateLabel(String label, String fullDomain) {
        // 레이블 길이: 63자 이하
        if (label.isEmpty() || label.length() > 63) {
            throw new UserServiceException(ErrorCode.INVALID_FORMAT,
                    String.format("Each domain label must be between 1 and 63 characters: %s", fullDomain));
        }

        // 사용 가능 문자: 영문자(A-Z, a-z), 숫자(0-9), 하이픈(-)만 사용 가능
        if (!label.matches("^[a-zA-Z0-9-]+$")) {
            throw new UserServiceException(ErrorCode.INVALID_FORMAT,
                    String.format("Domain label can only contain letters, numbers, and hyphens: %s", fullDomain));
        }

        // 하이픈으로 시작하거나 끝나면 안 됨
        if (label.startsWith("-") || label.endsWith("-")) {
            throw new UserServiceException(ErrorCode.INVALID_FORMAT,
                    String.format("Domain label cannot start or end with a hyphen: %s", fullDomain));
        }

        // xn--으로 시작해서는 안 됨
        if (label.toLowerCase().startsWith("xn--")) {
            throw new UserServiceException(ErrorCode.INVALID_FORMAT,
                    String.format("Domain label cannot start with 'xn--': %s", fullDomain));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Domain domain = (Domain) o;
        return Objects.equals(value, domain.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
