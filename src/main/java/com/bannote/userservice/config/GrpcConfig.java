package com.bannote.userservice.config;

import com.bannote.userservice.exception.GrpcExceptionHandler;
import com.bannote.userservice.interceptor.GrpcAuthInterceptor;
import io.grpc.ServerInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.grpc.server.GlobalServerInterceptor;

/**
 * gRPC 서버 설정
 * 전역 예외 핸들러 및 인증 인터셉터를 gRPC 서버에 등록
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class GrpcConfig {

    /**
     * 전역 예외 핸들러
     * Order 50으로 가장 먼저 실행되어 모든 예외를 캐치
     */
    @Bean
    @Order(50)
    @GlobalServerInterceptor
    ServerInterceptor grpcExceptionHandler() {
        return new GrpcExceptionHandler();
    }

    /**
     * 인증 인터셉터
     * Order 100으로 예외 핸들러 다음에 실행
     */
    @Bean
    @Order(100)
    @GlobalServerInterceptor
    ServerInterceptor grpcAuthInterceptor() {
        return new GrpcAuthInterceptor();
    }

}