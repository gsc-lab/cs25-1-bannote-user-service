package com.bannote.userservice.domain.studentclass;

import com.bannote.userservice.domain.department.Department;
import com.bannote.userservice.domain.studentclass.field.StudentClassCode;
import com.bannote.userservice.domain.studentclass.field.StudentClassName;
import com.bannote.userservice.domain.studentclass.field.StudentClassStatus;
import com.bannote.userservice.entity.StudentClassEntity;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.Year;

@Builder
@Getter
public class StudentClass {
    private final Long studentClassId;
    private final Department department;
    private final StudentClassCode studentClassCode;
    private final StudentClassName studentClassName;
    private final Year admissionYear;
    private final Year graduationYear;
    private final StudentClassStatus status;
    private final Timestamp createdAt;
    private final Timestamp deletedAt;
    private final String createdBy;
    private final String deletedBy;

    /**
     * 학급 생성시 사용
     */
    public static StudentClass create(
            Department department,
            StudentClassCode studentClassCode,
            StudentClassName studentClassName,
            Year admissionYear,
            Year graduationYear,
            StudentClassStatus status,
            String createdBy
    ) {
        return StudentClass.builder()
                .department(department)
                .studentClassCode(studentClassCode)
                .studentClassName(studentClassName)
                .admissionYear(admissionYear)
                .graduationYear(graduationYear)
                .status(status)
                .createdBy(createdBy)
                .build();
    }

    public static StudentClass fromStudentClassEntity(StudentClassEntity entity) {
        return StudentClass.builder()
                .studentClassId(entity.getId())
                .department(Department.fromDepartmentEntity(entity.getDepartment()))
                .studentClassCode(StudentClassCode.of(entity.getCode()))
                .studentClassName(StudentClassName.of(entity.getName()))
                .admissionYear(entity.getAdmissionYear())
                .graduationYear(entity.getGraduationYear())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .deletedAt(entity.getDeletedAt())
                .createdBy(entity.getCreatedBy())
                .deletedBy(entity.getDeletedBy())
                .build();
    }

    public com.bannote.userservice.proto.student_class.v1.StudentClass toProto() {

        return com.bannote.userservice.proto.student_class.v1.StudentClass.newBuilder()
                .setStudentClassCode(this.studentClassCode.getValue())
                .setDepartmentCode(this.department.getDepartmentCode().getValue())
                .setName(this.studentClassName.getValue())
                .setAdmissionYear(this.admissionYear.getValue())
                .setGraduationYear(this.graduationYear.getValue())
                .setStatus(this.status.toProto())
                .setCreatedAt(this.createdAt != null ? com.google.protobuf.util.Timestamps.fromMillis(this.createdAt.getTime()) : null)
                .build();
    }
}