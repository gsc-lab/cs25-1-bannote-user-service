package com.bannote.userservice.domain.allowedDomain;

import com.bannote.userservice.domain.allowedDomain.field.Domain;
import com.bannote.userservice.entity.AllowedDomainEntity;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class AllowedDomain {

    private final Domain domain;
    private final Timestamp createdAt;
    private final String createdBy;

    private AllowedDomain(
            Domain domain,
            Timestamp createdAt,
            String createdBy
    ) {
        this.domain = domain;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }

    public static AllowedDomain create(Domain domain) {
        return new AllowedDomain(domain, null, null);
    }

    public static AllowedDomain fromEntity(AllowedDomainEntity entity) {
        return new AllowedDomain(
                Domain.of(entity.getDomain()),
                entity.getCreatedAt(),
                entity.getCreatedBy()
        );
    }

    public com.bannote.userservice.proto.allowed_domain.v1.AllowedDomain toProto() {
        return com.bannote.userservice.proto.allowed_domain.v1.AllowedDomain.newBuilder()
                .setDomain(domain.getValue())
                .setCreatedBy(createdBy)
                .setCreatedAt(com.google.protobuf.util.Timestamps.fromMillis(createdAt.getTime()))
                .build();
    }
}
