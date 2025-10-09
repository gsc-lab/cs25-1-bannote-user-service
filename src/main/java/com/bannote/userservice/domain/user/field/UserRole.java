package com.bannote.userservice.domain.user.field;

import lombok.Getter;

// TODO: 서비스가 커지게 될 경우 권한 서비스로 분리 예정

@Getter
public enum UserRole {
    STUDENT(10),        // 학생
    DOORKEEPER(20),     // 키지기
    CLASS_REP(30),      // 반대표
    TA(40),             // 조교
    PROFESSOR(50),      // 교수
    ADMIN(60);          // 관리자

    private final int level;

    UserRole(int level) {
        this.level = level;
    }

    /**
     * 요구되는 권한 레벨을 충족하는지 확인
     */
    public boolean hasAuthority(int requiredLevel) {
        return this.level >= requiredLevel;
    }
}
