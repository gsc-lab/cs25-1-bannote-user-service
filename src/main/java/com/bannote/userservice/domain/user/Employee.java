package com.bannote.userservice.domain.user;

import com.bannote.userservice.domain.department.field.DepartmentCode;
import com.bannote.userservice.domain.department.field.DepartmentName;
import com.bannote.userservice.entity.UserEntity;
import lombok.Getter;
import lombok.With;

@Getter
public class Employee {
    private final UserBasic userBasic;
    private final DepartmentCode departmentCode;
    @With private final DepartmentName departmentName;

    private Employee(
            UserBasic userBasic,
            DepartmentCode departmentCode,
            DepartmentName departmentName
    ) {
        this.userBasic = userBasic;
        this.departmentCode = departmentCode;
        this.departmentName = departmentName;
    }

    public static Employee create(
            UserBasic userBasic,
            DepartmentCode departmentCode,
            DepartmentName departmentName
    ) {
        return new Employee(
                userBasic,
                departmentCode,
                departmentName
        );
    }

    public static Employee fromEntity(UserEntity userEntity) {
        UserBasic userBasic = UserBasic.fromEntity(userEntity);
        var department = userEntity.getEmployee().getDepartment();

        return new Employee(
                userBasic,
                DepartmentCode.of(department.getCode()),
                DepartmentName.of(department.getName())
        );
    }

    public com.bannote.userservice.proto.user.v1.Employee toProto() {
        return com.bannote.userservice.proto.user.v1.Employee.newBuilder()
                .setUser(this.userBasic.toProto())
                .setDepartmentCode(this.departmentCode.getValue())
                .setDepartmentName(this.departmentName.getValue())
                .build();
    }
}