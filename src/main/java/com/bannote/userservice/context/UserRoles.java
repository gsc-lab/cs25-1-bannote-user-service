package com.bannote.userservice.context;

import com.bannote.userservice.domain.user.field.UserRole;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserRoles {

    private final List<UserRole> userRoles;

    public UserRoles(List<UserRole> userRoles) {
        this.userRoles = Collections.unmodifiableList(new ArrayList<>(userRoles));
    }

    public boolean hasAuthority(int requiredLevel) {
        return userRoles.stream()
                .anyMatch(userRole -> userRole.hasAuthority(requiredLevel));
    }

    public List<UserRole> getRoles() {
        return userRoles;
    }
}
