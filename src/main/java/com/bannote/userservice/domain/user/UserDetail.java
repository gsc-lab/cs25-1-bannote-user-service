package com.bannote.userservice.domain.user;

import com.bannote.userservice.domain.department.field.DepartmentCode;
import com.bannote.userservice.domain.department.field.DepartmentName;
import com.bannote.userservice.domain.studentclass.field.StudentClassCode;
import com.bannote.userservice.domain.studentclass.field.StudentClassName;
import com.bannote.userservice.entity.DepartmentEntity;
import com.bannote.userservice.entity.StudentClassEntity;
import com.bannote.userservice.entity.UserEntity;
import lombok.Getter;

@Getter
public class UserDetail {

    private final UserBasic userBasic;

    private final StudentClassCode studentClassCode;
    private final StudentClassName studentClassName;

    private final DepartmentCode departmentCode;
    private final DepartmentName departmentName;

    private UserDetail(
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

    public static UserDetail ofBasic(
            UserBasic userBasic
    ) {
        return new UserDetail(
                userBasic,
                null,
                null,
                null,
                null
        );
    }

    public static UserDetail ofStudent(
            Student student
    ) {
        return new UserDetail(
                student.getUserBasic(),
                student.getStudentClassCode(),
                student.getStudentClassName(),
                student.getDepartmentCode(),
                student.getDepartmentName()
        );
    }

    public static UserDetail ofEmployee(
            Employee employee
    ) {
        return new UserDetail(
                employee.getUserBasic(),
                null,
                null,
                employee.getDepartmentCode(),
                employee.getDepartmentName()
        );
    }

    public static UserDetail fromEntity(UserEntity userEntity) {
        UserBasic userBasic = UserBasic.fromEntity(userEntity);
        StudentClassEntity studentClass = null;
        DepartmentEntity department = null;
        DepartmentEntity studentDepartment = null;

        if (userEntity.getType().isStudent() && userEntity.getStudent() != null) {
            studentClass = userEntity.getStudent().getStudentClass();
            if (studentClass != null) {
                studentDepartment = studentClass.getDepartment();
            }
        } else if (userEntity.getType().isEmployee() && userEntity.getEmployee() != null) {
            department = userEntity.getEmployee().getDepartment();
        }

        return new UserDetail(
                userBasic,
                studentClass != null ? StudentClassCode.of(studentClass.getCode()) : null,
                studentClass != null ? StudentClassName.of(studentClass.getName()) : null,
                studentDepartment != null ? DepartmentCode.of(studentDepartment.getCode())
                        : department != null ? DepartmentCode.of(department.getCode()) : null,
                studentDepartment != null ? DepartmentName.of(studentDepartment.getName())
                        : department != null ? DepartmentName.of(department.getName()) : null
        );
    }

    public boolean isStudent() {
        return userBasic.getUserType().isStudent();
    }

    public boolean isEmployee() {
        return userBasic.getUserType().isEmployee();
    }

    public boolean isService() {
        return userBasic.getUserType().isService();
    }

    public boolean isOther() {
        return userBasic.getUserType().isOther();
    }

    public boolean isLoginAllowed() {
        return userBasic.getUserStatus().loginAllowed();
    }

    public com.bannote.userservice.proto.user.v1.UserDetail toProto() {
        var builder = com.bannote.userservice.proto.user.v1.UserDetail.newBuilder();

        builder.setUserCode(this.userBasic.getUserCode().getValue());
        builder.setUserEmail(this.userBasic.getUserEmail().getValue());
        builder.setFamilyName(this.userBasic.getUserFamilyName().getValue());
        builder.setGivenName(this.userBasic.getUserGivenName().getValue());
        builder.setUserType(this.userBasic.getUserType().toProto());
        builder.setUserStatus(this.userBasic.getUserStatus().toProto());
        builder.setBio(this.userBasic.getUserBio() != null ? this.userBasic.getUserBio().getValue() : "");
        builder.setProfileImageUrl(this.userBasic.getUserProfileImage().getValue());
        builder.addAllUserRoles(this.userBasic.getUserRoles().toProtoList());

        if (this.userBasic.getCreatedAt() != null) {
            builder.setCreatedAt(com.google.protobuf.util.Timestamps.fromMillis(this.userBasic.getCreatedAt().getTime()));
        }

        if (this.userBasic.getDeletedAt() != null) {
            builder.setDeletedAt(com.google.protobuf.util.Timestamps.fromMillis(this.userBasic.getDeletedAt().getTime()));
        }

        if (isStudent()) {
            builder.setStudentClassCode(this.studentClassCode.getValue());
            builder.setStudentClassName(this.studentClassName.getValue());
            builder.setDepartmentCode(this.departmentCode.getValue());
            builder.setDepartmentName(this.departmentName.getValue());
        }

        if (isEmployee()) {
            builder.setDepartmentCode(this.departmentCode.getValue());
            builder.setDepartmentName(this.departmentName.getValue());
        }

        return builder.build();
    }
}