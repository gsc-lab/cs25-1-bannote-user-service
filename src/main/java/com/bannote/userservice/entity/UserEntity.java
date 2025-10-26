package com.bannote.userservice.entity;

import com.bannote.userservice.domain.user.field.UserBio;
import com.bannote.userservice.domain.user.field.UserStatus;
import com.bannote.userservice.domain.user.field.UserType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import java.sql.Timestamp;
import java.time.Instant;
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

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserRoleEntity> roles;

    @PrePersist
    void createdAt() {
        this.createdAt = Timestamp.from(Instant.now());
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
