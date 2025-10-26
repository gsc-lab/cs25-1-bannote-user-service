package com.bannote.userservice.domain.studentclass;

import com.bannote.userservice.domain.department.Department;
import com.bannote.userservice.domain.studentclass.field.StudentClassCode;
import com.bannote.userservice.domain.studentclass.field.StudentClassName;
import com.bannote.userservice.domain.studentclass.field.StudentClassStatus;
import com.bannote.userservice.entity.StudentClassEntity;
import lombok.Getter;
import lombok.With;

import java.sql.Timestamp;
import java.time.Year;

@Getter
public class StudentClass {
    private final Long studentClassId;
    private final Department department;
    private final StudentClassCode studentClassCode;
    @With private final StudentClassName studentClassName;
    @With private final Year admissionYear;
    @With private final Year graduationYear;
    @With private final StudentClassStatus status;
    private final Timestamp createdAt;
    private final Timestamp deletedAt;
    private final String createdBy;
    private final String deletedBy;

    private StudentClass(
            Long studentClassId,
            Department department,
            StudentClassCode studentClassCode,
            StudentClassName studentClassName,
            Year admissionYear,
            Year graduationYear,
            StudentClassStatus status,
            Timestamp createdAt,
            Timestamp deletedAt,
            String createdBy,
            String deletedBy
    ) {
        this.studentClassId = studentClassId;
        this.department = department;
        this.studentClassCode = studentClassCode;
        this.studentClassName = studentClassName;
        this.admissionYear = admissionYear;
        this.graduationYear = graduationYear;
        this.status = status;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
        this.createdBy = createdBy;
        this.deletedBy = deletedBy;
    }

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
        return new StudentClass(
                null,
                department,
                studentClassCode,
                studentClassName,
                admissionYear,
                graduationYear,
                status,
                null,
                null,
                createdBy,
                null
        );
    }

    public static StudentClass fromEntity(StudentClassEntity entity) {
        return new StudentClass(
                entity.getId(),
                Department.fromEntity(entity.getDepartment()),
                StudentClassCode.of(entity.getCode()),
                StudentClassName.of(entity.getName()),
                entity.getAdmissionYear(),
                entity.getGraduationYear(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getDeletedAt(),
                entity.getCreatedBy(),
                entity.getDeletedBy()
        );
    }

    public com.bannote.userservice.proto.student_class.v1.StudentClass toProto() {

        return com.bannote.userservice.proto.student_class.v1.StudentClass.newBuilder()
                .setStudentClassCode(this.studentClassCode.getValue())
                .setDepartmentCode(this.department.getDepartmentCode().getValue())
                .setName(this.studentClassName.getValue())
                .setAdmissionYear(this.admissionYear.getValue())
                .setGraduationYear(this.graduationYear.getValue())
                .setStatus(this.status.toProto())
                .setCreatedAt(com.google.protobuf.util.Timestamps.fromMillis(this.createdAt.getTime()))
                .build();
    }
}