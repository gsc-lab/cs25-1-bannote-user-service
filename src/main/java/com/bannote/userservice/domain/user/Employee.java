package com.bannote.userservice.domain.user;

import com.bannote.userservice.domain.department.field.DepartmentCode;
import com.bannote.userservice.domain.department.field.DepartmentName;
import com.bannote.userservice.domain.studentclass.field.StudentClassCode;
import com.bannote.userservice.domain.studentclass.field.StudentClassName;
import com.bannote.userservice.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Employee {
    private final UserBasic userBasic;
    private final DepartmentCode departmentCode;
    private final DepartmentName departmentName;

    public static Employee create(
            UserBasic userBasic,
            DepartmentCode departmentCode,
            DepartmentName departmentName
    ) {
        return Employee.builder()
                .userBasic(userBasic)
                .departmentCode(departmentCode)
                .departmentName(departmentName)
                .build();
    }

    public static Employee fromEntity(UserEntity userEntity) {
        UserBasic userBasic = UserBasic.fromEntity(userEntity);
        var department = userEntity.getEmployee().getDepartment();

        return Employee.builder()
                .userBasic(userBasic)
                .departmentCode(DepartmentCode.of(department.getCode()))
                .departmentName(DepartmentName.of(department.getName()))
                .build();
    }

    public com.bannote.userservice.proto.user.v1.Employee toProto() {
        return com.bannote.userservice.proto.user.v1.Employee.newBuilder()
                .setUser(this.userBasic.toProto())
                .setDepartmentCode(this.departmentCode.getValue())
                .setDepartmentName(this.departmentName.getValue())
                .build();
    }
}