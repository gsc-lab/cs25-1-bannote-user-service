package com.bannote.userservice.config;

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
 * 인증 인터셉터를 gRPC 서버에 등록
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class GrpcConfig {

    @Bean
    @Order(100)
    @GlobalServerInterceptor
    ServerInterceptor grpcAuthInterceptor() {
        return new GrpcAuthInterceptor();
    }

}