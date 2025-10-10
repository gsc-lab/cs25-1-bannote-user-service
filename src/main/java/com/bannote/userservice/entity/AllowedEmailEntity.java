package com.bannote.userservice.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
public class AllowedEmailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @PrePersist
    void createdAt() {
        this.createdAt = Timestamp.from(Instant.now());
    }

    public static AllowedEmailEntity create(
            String email,
            String createdBy
    ) {
        AllowedEmailEntity allowedEmailEntity = new AllowedEmailEntity();
        allowedEmailEntity.email = email;
        allowedEmailEntity.createdBy = createdBy;
        return allowedEmailEntity;
    }
}
