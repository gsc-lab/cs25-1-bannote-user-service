package com.bannote.userservice.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
public class AllowedEmailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @CreatedBy
    @Column(name = "created_by")
    private Long createdBy;

    @PrePersist
    void createdAt() {
        this.createdAt = Timestamp.from(Instant.now());
    }
}
