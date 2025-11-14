package com.bannote.userservice.entity;

import com.bannote.userservice.domain.user.field.UserRole;
import com.bannote.userservice.domain.user.field.UserStatus;
import com.bannote.userservice.domain.user.field.UserType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "\"user\"", indexes = {
        @Index(name = "idx_userentity_unq", columnList = "user_code", unique = true)
})
@Getter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE \"user\" SET deleted_at = NOW() where id=?")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Setter
    @Column(name = "name")
    private String name;

    @Setter
    @Column(name = "family_name")
    private String familyName;

    @Setter
    @Column(name = "given_name")
    private String givenName;

    @Setter
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType type;

    @Setter
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Setter
    @Column(name = "bio")
    private String bio;

    @Setter
    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @Setter
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private StudentEntity student;

    @Setter
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private EmployeeEntity employee;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRoleEntity> roles = new ArrayList<>();

    @PrePersist
    void createdAt() {
        this.createdAt = Timestamp.from(Instant.now());
    }

    /**
     * 사용자에게 역할 추가
     * @param role 추가할 역할
     * @param createdBy 역할을 부여한 사용자 ID (시스템이 부여하는 경우 null)
     */
    public void addRole(UserRole role, String createdBy) {
        // 중복 체크
        boolean exists = this.roles.stream()
                .anyMatch(r -> r.getRole() == role);

        if (!exists) {
            UserRoleEntity userRoleEntity = UserRoleEntity.create(this, role, createdBy);
            this.roles.add(userRoleEntity);
        }
    }

    public static UserEntity create(
            String code,
            String email,
            String name,
            String familyName,
            String givenName,
            UserType type,
            UserStatus status,
            String bio,
            String profileImage
    ) {
        UserEntity user = new UserEntity();
        user.code = code;
        user.email = email;
        user.name = name;
        user.familyName = familyName;
        user.givenName = givenName;
        user.type = type;
        user.status = status;
        user.bio = bio;
        user.profileImage = profileImage;
        return user;
    }

}
