package com.bannote.userservice.context;

import com.bannote.userservice.domain.user.field.UserCode;
import com.bannote.userservice.domain.user.field.UserRole;

/**
 * gRPC 메타데이터에서 추출한 사용자 인증 정보
 *
 * @param userCode 사용자 코드 (x-user-code)
 * @param userRoles 사용자 권한 (x-user-role)
 */
public record AuthInfo(
    UserCode userCode,
    UserRoles userRoles
) {
    /**
     * 요구되는 권한 레벨을 충족하는지 확인
     */
    public boolean hasAuthority(int requiredLevel) {
        return userRoles.hasAuthority(requiredLevel);
    }

    /**
     * 특정 권한 이상인지 확인
     */
    public boolean hasAuthority(UserRole requiredRole) {
        return userRoles.hasAuthority(requiredRole.getLevel());
    }

    /**
     * 현재 가진 권한 문자열로 반환
     */
    public String getRolesToString() {
        return this.userRoles.toString();
    }
}