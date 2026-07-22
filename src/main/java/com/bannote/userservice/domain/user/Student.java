package com.bannote.userservice.domain.user;

import com.bannote.userservice.domain.department.field.DepartmentCode;
import com.bannote.userservice.domain.department.field.DepartmentName;
import com.bannote.userservice.domain.studentclass.field.StudentClassCode;
import com.bannote.userservice.domain.studentclass.field.StudentClassName;
import com.bannote.userservice.entity.UserEntity;
import lombok.Getter;
import lombok.With;

@Getter
public class Student {
    private final UserBasic userBasic;
    private final StudentClassCode studentClassCode;
    @With private final StudentClassName studentClassName;
    private final DepartmentCode departmentCode;
    @With private final DepartmentName departmentName;

    private Student(
            UserBasic userBasic,
            StudentClassCode studentClassCode,
            StudentClassName studentClassName,
            DepartmentCode departmentCode,
            DepartmentName departmentName
    ) {
        this.userBasic = userBasic;
        this.studentClassCode = studentClassCode;
        this.studentClassName = studentClassName;
        this.departmentCode = departmentCode;
        this.departmentName = departmentName;
    }

    public static Student create(
            UserBasic userBasic,
            StudentClassCode studentClassCode,
            DepartmentCode departmentCode
    ) {
        return new Student(
                userBasic,
                studentClassCode,
                null,
                departmentCode,
                null
        );
    }

    public static Student fromEntity(UserEntity userEntity) {
        UserBasic userBasic = UserBasic.fromEntity(userEntity);
        var studentClass = userEntity.getStudent().getStudentClass();
        var department = studentClass.getDepartment();

        return new Student(
                userBasic,
                StudentClassCode.of(studentClass.getCode()),
                StudentClassName.of(studentClass.getName()),
                DepartmentCode.of(department.getCode()),
                DepartmentName.of(department.getName())
        );
    }

    public com.bannote.userservice.proto.user.v1.Student toProto() {
        return com.bannote.userservice.proto.user.v1.Student.newBuilder()
                .setUser(this.userBasic.toProto())
                .setStudentClassCode(this.studentClassCode.getValue())
                .setStudentClassName(this.studentClassName.getValue())
                .setDepartmentCode(this.departmentCode.getValue())
                .setDepartmentName(this.departmentName.getValue())
                .build();
    }
}