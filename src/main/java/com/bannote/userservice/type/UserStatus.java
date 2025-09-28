package com.bannote.userservice.type;

public enum UserStatus {
    PENDING,        // 가입 대기
    ACTIVE,         // 재학/재직 (활성)
    GRADUATED,      // 졸업
    LEAVE,          // 휴학/휴직
    SUSPENDED,      // 정학/정직
    WITHDRAWN,      // 자퇴/퇴사
    EXPELLED        // 제적/해고
}
