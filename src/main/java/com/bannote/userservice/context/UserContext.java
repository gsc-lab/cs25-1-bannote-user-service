package com.bannote.userservice.context;

import lombok.extern.slf4j.Slf4j;

/**
 * 스레드별 사용자 인증 정보를 저장하는 컨텍스트
 * gRPC 요청 처리 중 현재 인증된 사용자 정보에 접근할 수 있도록 ThreadLocal 사용
 */
@Slf4j
public class UserContext {

    private static final ThreadLocal<AuthInfo> authInfoHolder = new ThreadLocal<>();

    private UserContext() {
        // utility class
    }

    /**
     * 현재 스레드에 사용자 인증 정보 저장
     */
    public static void setAuthInfo(AuthInfo authInfo) {
        authInfoHolder.set(authInfo);
        log.debug("AuthInfo set for user: {}", authInfo.userCode().getValue());
    }

    /**
     * 현재 스레드의 사용자 인증 정보 조회
     */
    public static AuthInfo getAuthInfo() {
        return authInfoHolder.get();
    }

    /**
     * 현재 스레드의 사용자 인증 정보 제거
     * 요청 처리 완료 후 반드시 호출하여 메모리 누수 방지
     */
    public static void clear() {
        AuthInfo authInfo = authInfoHolder.get();
        if (authInfo != null) {
            log.debug("AuthInfo cleared for user: {}", authInfo.userCode().getValue());
        }
        authInfoHolder.remove();
    }

    /**
     * 인증 정보가 존재하는지 확인
     */
    public static boolean isAuthenticated() {
        return authInfoHolder.get() != null;
    }
}