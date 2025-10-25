package com.bannote.userservice.entity;

import com.bannote.userservice.domain.studentclass.field.StudentClassStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.Year;

@Entity
@Table(name = "\"student_class\"")
@SQLDelete(sql = "UPDATE \"student_class\" SET deleted_at = NOW() where id=?")
@Getter
public class StudentClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @Setter
    @Column(name = "admission_year", nullable = false)
    private Year admissionYear;

    @Setter
    @Column(name = "graduation_year", nullable = false)
    private Year graduationYear;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StudentClassStatus status;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @Column(name = "created_by")
    private String createdBy;

    @Setter
    @Column(name = "deleted_by")
    private String deletedBy;

    @PrePersist
    void createdAt() {
        this.createdAt = Timestamp.from(Instant.now());
    }

    public static StudentClassEntity create(
            DepartmentEntity department,
            String code,
            String name,
            Year admissionYear,
            Year graduationYear,
            StudentClassStatus status,
            String createdBy
    ) {
        StudentClassEntity studentClassEntity = new StudentClassEntity();
        studentClassEntity.department = department;
        studentClassEntity.code = code;
        studentClassEntity.name = name;
        studentClassEntity.admissionYear = admissionYear;
        studentClassEntity.graduationYear = graduationYear;
        studentClassEntity.status = status;
        studentClassEntity.createdBy = createdBy;
        return studentClassEntity;
    }
}
