package com.bannote.userservice.entity;

import com.bannote.userservice.type.UserRole;
import com.bannote.userservice.type.UserStatus;
import com.bannote.userservice.type.UserType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Entity
@Table(name = "\"user\"", indexes = {
        @Index(name = "idx_userentity_unq", columnList = "user_number", unique = true)
})
@Getter
@Setter
@SQLDelete(sql = "UPDATE \"user\" SET deleted_at = NOW() where id=?")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_number")
    private String userNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "family_name")
    private String familyName;

    @Column(name = "given_name")
    private String givenName;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private UserType type;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "bio")
    private String bio;

    @Column(name = "profile")
    private String profile;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private StudentEntity student;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private EmployeeEntity employee;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserRoleEntity> roles;

    @PrePersist
    void createdAt() {
        this.createdAt = Timestamp.from(Instant.now());
    }

    /**
     * 가장 높은 권한을 가진 역할 반환
     */
    public UserRole getHighestRole() {
        return roles.stream()
                .map(UserRoleEntity::getRole)
                .max(Comparator.comparingInt(UserRole::getLevel))
                .orElse(null);
    }

}
