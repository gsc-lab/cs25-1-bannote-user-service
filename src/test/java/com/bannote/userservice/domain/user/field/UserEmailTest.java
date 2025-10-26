package com.bannote.userservice.domain.user.field;

import com.bannote.userservice.exception.ErrorCode;
import com.bannote.userservice.exception.UserServiceException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class UserEmailTest {

    @Test
    void valid_email_1() {
        String email = "kyumin12271227@g.yju.ac.kr";

        Assertions.assertThatCode(() -> UserEmail.of(email))
                .doesNotThrowAnyException();
    }

    @Test
    void valid_email_2() {
        String email = "example@example.com";

        Assertions.assertThatCode(() -> UserEmail.of(email))
                .doesNotThrowAnyException();
    }

    @Test
    void invalid_email_null() {
        String email = null;

        Assertions.assertThatThrownBy(() -> UserEmail.of(email))
                .isInstanceOf(UserServiceException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.REQUIRED_FIELD_MISSING);
    }

    @Test
    void invalid_email_empty() {
        String email = "";

        Assertions.assertThatThrownBy(() -> UserEmail.of(email))
                .isInstanceOf(UserServiceException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.REQUIRED_FIELD_MISSING);
    }

    @Test
    void invalid_email_format_1() {
        String email = "invalid-email";

        Assertions.assertThatThrownBy(() -> UserEmail.of(email))
                .isInstanceOf(UserServiceException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_FORMAT);
    }

    @Test
    void invalid_email_format_2() {
        String email = "invalid-email@";

        Assertions.assertThatThrownBy(() -> UserEmail.of(email))
                .isInstanceOf(UserServiceException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_FORMAT);
    }

    @Test
    void invalid_email_format_3() {
        String email = "@invalid.com";

        Assertions.assertThatThrownBy(() -> UserEmail.of(email))
                .isInstanceOf(UserServiceException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_FORMAT);
    }

    @Test
    void invalid_email_format_4() {
        String email = "@example@invalid.com";

        Assertions.assertThatThrownBy(() -> UserEmail.of(email))
                .isInstanceOf(UserServiceException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_FORMAT);
    }

    @Test
    void getDomain_valid_email() {
        String email = "kyumin12271227@g.yju.ac.kr";
        UserEmail userEmail = UserEmail.of(email);

        String domain = userEmail.getDomain();

        Assertions.assertThat(domain).isEqualTo("g.yju.ac.kr");
    }

    @Test
    void getDomain_simple_email() {
        String email = "test@example.com";
        UserEmail userEmail = UserEmail.of(email);

        String domain = userEmail.getDomain();

        Assertions.assertThat(domain).isEqualTo("example.com");
    }

}
