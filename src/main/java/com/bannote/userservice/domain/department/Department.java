package com.bannote.userservice.domain.department;

import com.bannote.userservice.domain.department.field.DepartmentCode;
import com.bannote.userservice.domain.department.field.DepartmentName;
import com.bannote.userservice.entity.DepartmentEntity;
import lombok.Getter;
import lombok.With;

import java.sql.Timestamp;

@Getter
public class Department {
    private final Long departmentId;
    private final DepartmentCode departmentCode;
    @With private final DepartmentName departmentName;
    private final Timestamp createdAt;
    private final Timestamp deletedAt;

    private Department(
            Long departmentId,
            DepartmentCode departmentCode,
            DepartmentName departmentName,
            Timestamp createdAt,
            Timestamp deletedAt
    ) {
        this.departmentId = departmentId;
        this.departmentCode = departmentCode;
        this.departmentName = departmentName;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
    }

    public static Department create(
            DepartmentCode departmentCode,
            DepartmentName departmentName
    ) {
        return new Department(
                null,
                departmentCode,
                departmentName,
                null,
                null
        );
    }

    public static Department fromEntity(DepartmentEntity entity) {
        return new Department(
                entity.getId(),
                DepartmentCode.of(entity.getCode()),
                DepartmentName.of(entity.getName()),
                entity.getCreatedAt(),
                entity.getDeletedAt()
        );
    }

    public com.bannote.userservice.proto.department.v1.Department toProto() {

        return com.bannote.userservice.proto.department.v1.Department.newBuilder()
                .setDepartmentCode(this.departmentCode.getValue())
                .setName(this.departmentName.getValue())
                .setCreatedAt(this.createdAt != null ? com.google.protobuf.util.Timestamps.fromMillis(this.createdAt.getTime()) : null)
                .build();
    }
}