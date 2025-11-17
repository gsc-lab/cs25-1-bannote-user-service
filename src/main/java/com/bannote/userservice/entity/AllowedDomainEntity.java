package com.bannote.userservice.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Getter
public class AllowedDomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "domain", unique = true, nullable = false)
    private String domain;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @PrePersist
    void createdAt() {
        this.createdAt = Timestamp.from(Instant.now());
    }

    public static AllowedDomainEntity create(
            String domain,
            String createdBy
    ) {
        AllowedDomainEntity allowedDomain = new AllowedDomainEntity();
        allowedDomain.domain = domain;
        allowedDomain.createdBy = createdBy;
        return allowedDomain;
    }
}
