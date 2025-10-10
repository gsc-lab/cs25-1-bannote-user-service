package com.bannote.userservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class StudentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @OneToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_class_id")
    private StudentClassEntity studentClass;

    public static StudentEntity create(
            UserEntity user,
            StudentClassEntity studentClass
    ) {
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.user = user;
        studentEntity.studentClass = studentClass;
        return studentEntity;
    }
}
