package com.bannote.userservice.entity;

import com.bannote.userservice.domain.user.field.UserRole;
import jakarta.persistence.*;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "\"user_role\"",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_role", columnNames = {"user_id", "role"})
    })
@Getter
public class UserRoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @PrePersist
    void createdAt() {
        this.createdAt = Timestamp.from(Instant.now());
    }

    public static UserRoleEntity create(
            UserEntity user,
            UserRole role,
            String createdBy
    ) {
        UserRoleEntity userRoleEntity = new UserRoleEntity();
        userRoleEntity.user = user;
        userRoleEntity.role = role;
        userRoleEntity.createdBy = createdBy;
        return userRoleEntity;
    }
}
