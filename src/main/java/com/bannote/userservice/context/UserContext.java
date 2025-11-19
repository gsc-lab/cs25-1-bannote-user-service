package com.bannote.userservice.context;

import io.grpc.Context;
import lombok.extern.slf4j.Slf4j;

/**
 * gRPC 요청별 사용자 인증 정보를 저장하는 컨텍스트
 * gRPC 요청 처리 중 현재 인증된 사용자 정보에 접근할 수 있도록 gRPC Context 사용
 */
@Slf4j
public class UserContext {

    private static final Context.Key<AuthInfo> AUTH_INFO_KEY = Context.key("authInfo");

    private UserContext() {
        // utility class
    }

    /**
     * gRPC Context Key 반환
     * GrpcAuthInterceptor에서 Context 설정 시 사용
     */
    public static Context.Key<AuthInfo> getKey() {
        return AUTH_INFO_KEY;
    }

    /**
     * 현재 gRPC Context의 사용자 인증 정보 조회
     */
    public static AuthInfo getAuthInfo() {
        return AUTH_INFO_KEY.get();
    }

    /**
     * 인증 정보가 존재하는지 확인
     */
    public static boolean isAuthenticated() {
        return AUTH_INFO_KEY.get() != null;
    }
}