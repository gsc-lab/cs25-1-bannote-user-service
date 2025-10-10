package com.bannote.userservice.domain.department;

import com.bannote.userservice.domain.department.field.DepartmentCode;
import com.bannote.userservice.domain.department.field.DepartmentName;
import com.bannote.userservice.entity.DepartmentEntity;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Builder
@Getter
public class Department {
    private final Long departmentId;
    private final DepartmentCode departmentCode;
    private final DepartmentName departmentName;
    private final Timestamp createdAt;
    private final Timestamp deletedAt;

    public static Department create(
            DepartmentCode departmentCode,
            DepartmentName departmentName
    ) {
        return Department.builder()
                .departmentCode(departmentCode)
                .departmentName(departmentName)
                .build();
    }

    public static Department fromDepartmentEntity(DepartmentEntity entity) {
        return Department.builder()
                .departmentId(entity.getId())
                .departmentCode(DepartmentCode.of(entity.getCode()))
                .departmentName(DepartmentName.of(entity.getName()))
                .createdAt(entity.getCreatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }

    public com.bannote.userservice.proto.department.v1.Department toProto() {

        return com.bannote.userservice.proto.department.v1.Department.newBuilder()
                .setDepartmentCode(this.departmentCode.getValue())
                .setName(this.departmentName.getValue())
                .setCreatedAt(this.createdAt != null ? com.google.protobuf.util.Timestamps.fromMillis(this.createdAt.getTime()) : null)
                .build();
    }
}