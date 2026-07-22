package com.bannote.userservice.domain.user.field;

import com.bannote.userservice.entity.UserRoleEntity;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserRoles {

    @Getter
    private final List<UserRole> value;

    private UserRoles(List<UserRole> roles) {
        this.value = new ArrayList<>(roles);
    }

    /**
     * UserRoleEntity 리스트로부터 UserRoles 생성
     */
    public static UserRoles of(List<UserRoleEntity> userRoleEntities) {
        if (userRoleEntities == null || userRoleEntities.isEmpty()) {
            return new UserRoles(Collections.emptyList());
        }

        List<UserRole> roles = userRoleEntities.stream()
                .map(UserRoleEntity::getRole)
                .collect(Collectors.toList());

        return new UserRoles(roles);
    }

    /**
     * 빈 UserRoles 생성
     */
    public static UserRoles empty() {
        return new UserRoles(Collections.emptyList());
    }

    /**
     * 특정 역할을 가지고 있는지 확인
     */
    public boolean hasRole(UserRole role) {
        return value.contains(role);
    }

    /**
     * 특정 권한 레벨 이상인지 확인
     */
    public boolean hasAuthority(int requiredLevel) {
        return value.stream()
                .anyMatch(role -> role.hasAuthority(requiredLevel));
    }

    /**
     * 역할 리스트를 불변 리스트로 반환
     */
    public List<UserRole> getValues() {
        return Collections.unmodifiableList(value);
    }

    /**
     * Proto UserRole 리스트로 변환
     */
    public List<com.bannote.userservice.proto.common.v1.UserRole> toProtoList() {

        return value.stream()
                .map(UserRole::toProto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserRoles userRoles = (UserRoles) o;
        return Objects.equals(value, userRoles.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
