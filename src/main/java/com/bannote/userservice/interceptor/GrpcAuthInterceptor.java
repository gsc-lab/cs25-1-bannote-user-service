package com.bannote.userservice.interceptor;

import com.bannote.userservice.context.AuthInfo;
import com.bannote.userservice.context.UserContext;
import com.bannote.userservice.context.UserRoles;
import com.bannote.userservice.domain.user.field.UserCode;
import com.bannote.userservice.domain.user.field.UserRole;
import com.bannote.userservice.exception.ErrorCode;
import com.bannote.userservice.exception.UserServiceException;
import io.grpc.*;
import io.grpc.Contexts;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * gRPC 요청의 메타데이터에서 사용자 인증 정보를 추출하고 권한을 확인하는 인터셉터
 *
 * 메타데이터 키:
 * - x-user-code: 사용자 코드 (7자리)
 * - x-user-role: 사용자 권한 (STUDENT, PROFESSOR, ADMIN 등)
 */
@Slf4j
public class GrpcAuthInterceptor implements ServerInterceptor {

    private static final Metadata.Key<String> USER_CODE_KEY =
        Metadata.Key.of("x-user-code", Metadata.ASCII_STRING_MARSHALLER);

    private static final Metadata.Key<String> USER_ROLE_KEY =
        Metadata.Key.of("x-user-role", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        // 서비스 이름 확인
        String fullMethodName = call.getMethodDescriptor().getFullMethodName();
        log.debug("Processing gRPC call: {}", fullMethodName);
        System.out.println("Processing gRPC call " + fullMethodName);

        // TODO 별도의 메서드로 분리
        // UserService와 Health 서비스는 인증 제외
        if (fullMethodName.endsWith("UserService/UserLogin") ||
            fullMethodName.endsWith("UserService/CreateUser") ||
            fullMethodName.endsWith("DepartmentService/GetDepartment") ||
            fullMethodName.endsWith("DepartmentService/ListDepartments") ||
            fullMethodName.endsWith("DepartmentService/GetManyDepartments") ||
            fullMethodName.endsWith("StudentClassService/GetStudentClass") ||
            fullMethodName.endsWith("StudentClassService/ListStudentClasses") ||
            fullMethodName.endsWith("StudentClassService/GetManyStudentClasses") ||
            fullMethodName.endsWith("AllowedDomainService/CheckAllowedDomain") ||
            fullMethodName.startsWith("grpc.health.v1.Health/")) {
            log.debug("Skipping authentication for: {}", fullMethodName);
            return next.startCall(call, headers);
        }

        try {
            // 메타데이터에서 사용자 정보 추출
            String userCodeValue = headers.get(USER_CODE_KEY);
            String userRoleValue = headers.get(USER_ROLE_KEY);

            // 필수 헤더 검증
            validateRequiredHeaders(userCodeValue, userRoleValue);

            // 도메인 객체 생성
            UserCode userCode = UserCode.of(userCodeValue);
            UserRoles userRoles = parseUserRoles(userRoleValue);

            // AuthInfo 생성
            AuthInfo authInfo = new AuthInfo(userCode, userRoles);

            log.info("Authentication successful - UserCode: {}, UserRole: {}",
                userCode.getValue(), userRoles);

            // gRPC Context에 AuthInfo 저장하고 다음 핸들러 호출
            Context context = Context.current().withValue(UserContext.getKey(), authInfo);
            return Contexts.interceptCall(context, call, headers, next);

        } catch (UserServiceException e) {
            log.error("Authentication failed: {}", e.getMessage());
            call.close(
                Status.UNAUTHENTICATED.withDescription(e.getMessage()),
                new Metadata()
            );
            return new ServerCall.Listener<>() {};
        } catch (Exception e) {
            log.error("Unexpected error during authentication", e);
            call.close(
                Status.INTERNAL.withDescription("Authentication error: " + e.getMessage()),
                new Metadata()
            );
            return new ServerCall.Listener<>() {};
        }
    }

    /**
     * 필수 헤더 존재 여부 검증
     */
    private void validateRequiredHeaders(String userCode, String userRole) {
        if (userCode == null || userCode.isEmpty()) {
            throw new UserServiceException(
                ErrorCode.REQUIRED_FIELD_MISSING,
                "x-user-code header is required"
            );
        }
        if (userRole == null || userRole.isEmpty()) {
            throw new UserServiceException(
                ErrorCode.REQUIRED_FIELD_MISSING,
                "x-user-role header is required"
            );
        }
    }

    /**
     * 문자열 값을 UserRole enum으로 변환
     * 쉼표로 구분된 권한 문자열을 파싱하여 UserRoles 객체로 변환
     * 예: "STUDENT,CLASS_REP" -> UserRoles([STUDENT, CLASS_REP])
     */
    private UserRoles parseUserRoles(String roleValue) {
        try {
            List<UserRole> roles = Arrays.stream(roleValue.split(","))
                    .map(String::trim)
                    .map(String::toUpperCase)
                    .map(UserRole::valueOf)
                    .toList();

            return new UserRoles(roles);
        } catch (IllegalArgumentException e) {
            throw new UserServiceException(
                ErrorCode.INVALID_FORMAT,
                "Invalid user role: " + roleValue
            );
        }
    }
}