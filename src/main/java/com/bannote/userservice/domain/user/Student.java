package com.bannote.userservice.domain.user;

import com.bannote.userservice.domain.department.field.DepartmentCode;
import com.bannote.userservice.domain.department.field.DepartmentName;
import com.bannote.userservice.domain.studentclass.field.StudentClassCode;
import com.bannote.userservice.domain.studentclass.field.StudentClassName;
import com.bannote.userservice.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

/**
 * 학생 도메인 (UserBasic + 학생 정보)
 * Proto Student 메시지와 동일한 구조
 */
@Builder
@Getter
public class Student {
    private final UserBasic userBasic;
    private final StudentClassCode studentClassCode;
    private final StudentClassName studentClassName;
    private final DepartmentCode departmentCode;
    private final DepartmentName departmentName;

    public static Student create(
            UserBasic userBasic,
            StudentClassCode studentClassCode,
            StudentClassName studentClassName,
            DepartmentCode departmentCode,
            DepartmentName departmentName
    ) {
        return Student.builder()
                .userBasic(userBasic)
                .studentClassCode(studentClassCode)
                .studentClassName(studentClassName)
                .departmentCode(departmentCode)
                .departmentName(departmentName)
                .build();
    }

    public static Student fromEntity(UserEntity userEntity) {
        UserBasic userBasic = UserBasic.fromEntity(userEntity);
        var studentClass = userEntity.getStudent().getStudentClass();
        var department = studentClass.getDepartment();

        return Student.builder()
                .userBasic(userBasic)
                .studentClassCode(StudentClassCode.of(studentClass.getCode()))
                .studentClassName(StudentClassName.of(studentClass.getName()))
                .departmentCode(DepartmentCode.of(department.getCode()))
                .departmentName(DepartmentName.of(department.getName()))
                .build();
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