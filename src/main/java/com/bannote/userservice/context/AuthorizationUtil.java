package com.bannote.userservice.context;

import com.bannote.userservice.domain.user.field.UserRole;
import com.bannote.userservice.exception.ErrorCode;
import com.bannote.userservice.exception.UserServiceException;
import lombok.extern.slf4j.Slf4j;

/**
 * 권한 확인을 위한 유틸리티 클래스
 */
@Slf4j
public class AuthorizationUtil {

    private AuthorizationUtil() {
        // utility class
    }

    /**
     * 현재 인증된 사용자 정보 조회
     * 인증되지 않은 경우 예외 발생
     */
    public static AuthInfo getCurrentAuthInfo() {
        AuthInfo authInfo = UserContext.getAuthInfo();
        if (authInfo == null) {
            throw new UserServiceException(
                ErrorCode.UNAUTHORIZED,
                "Authentication is required"
            );
        }
        return authInfo;
    }

    /**
     * 현재 인증된 사용자가 특정 권한을 이상인지 확인
     * @param requiredRole 필요한 권한
     */
    public static void requireAuthority(UserRole requiredRole) {
        AuthInfo authInfo = getCurrentAuthInfo();
        if (!authInfo.hasAuthority(requiredRole)) {
            log.warn("Access denied - User {} tried to access {}'s resource without sufficient authority",
                    authInfo.userCode().getValue(), requiredRole);
            throw new UserServiceException(
                    ErrorCode.FORBIDDEN,
                    String.format("Required role: %s, but current roles: %s",
                            requiredRole,
                            authInfo.getRolesToString())
            );
        }
    }

    /**
     * ADMIN 권한 확인
     */
    public static void requireAdmin() {
        requireAuthority(UserRole.ADMIN);
    }

    /**
     * PROFESSOR 이상 권한 확인
     */
    public static void requireProfessor() {
        requireAuthority(UserRole.PROFESSOR);
    }

    /**
     * TA 이상 권한 확인
     */
    public static void requireTA() {
        requireAuthority(UserRole.TA);
    }

    /**
     * CLASS_REP 이상 권한 확인
     */
    public static void requireClassRep() {
        requireAuthority(UserRole.CLASS_REP);
    }

    /**
     * DOORKEEPER 이상 권한 확인
     */
    public static void requireDoorkeeper() {
        requireAuthority(UserRole.DOORKEEPER);
    }

    /**
     * 특정 사용자 본인인지 확인
     * 본인이 아닌 경우 예외 발생
     */
    public static void requireSameUser(String targetUserCode) {
        AuthInfo authInfo = getCurrentAuthInfo();
        if (!authInfo.userCode().getValue().equals(targetUserCode)) {
            log.warn("Access denied - User {} tried to access {}'s resource",
                authInfo.userCode().getValue(), targetUserCode);
            throw new UserServiceException(
                ErrorCode.FORBIDDEN,
                "Can only access own resources"
            );
        }
    }

    /**
     * 본인이거나 특정 권한 이상인지 확인
     * 예: 본인 또는 PROFESSOR 이상만 접근 가능
     */
    public static void requireSameUserOrAuthority(String targetUserCode, UserRole requiredRole) {
        AuthInfo authInfo = getCurrentAuthInfo();

        // 본인인 경우 허용
        if (authInfo.userCode().getValue().equals(targetUserCode)) {
            return;
        }

        // 본인이 아닌 경우 권한 확인
        if (!authInfo.hasAuthority(requiredRole)) {
            log.warn("Access denied - User {} tried to access {}'s resource without sufficient authority",
                authInfo.userCode().getValue(), targetUserCode);
            throw new UserServiceException(
                    ErrorCode.FORBIDDEN,
                    String.format("Requires %s authority or being the resource owner, but current roles: %s",
                            requiredRole,
                            authInfo.getRolesToString())
            );
        }
    }
}