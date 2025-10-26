package com.bannote.userservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class StudentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private DepartmentEntity department;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_class_id", nullable = false)
    private StudentClassEntity studentClass;

    public static StudentEntity create(
            UserEntity user,
            StudentClassEntity studentClass
    ) {
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.user = user;
        studentEntity.department = studentClass.getDepartment();
        studentEntity.studentClass = studentClass;
        return studentEntity;
    }
}
