package com.bannote.userservice.entity;

import com.bannote.userservice.type.StudentClassStatus;
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
@Setter
public class StudentClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "admission_year")
    private Year admissionYear;

    @Column(name = "graduation_year")
    private Year graduationYear;

    @Column(name = "status")
    private StudentClassStatus status;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "deleted_by")
    private Long deletedBy;

    @PrePersist
    void createdAt() {
        this.createdAt = Timestamp.from(Instant.now());
    }
}
