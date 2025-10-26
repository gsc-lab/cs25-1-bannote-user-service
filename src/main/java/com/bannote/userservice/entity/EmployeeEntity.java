package com.bannote.userservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class EmployeeEntity {

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

    public static EmployeeEntity create(
            UserEntity user,
            DepartmentEntity department
    ) {
        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.user = user;
        employeeEntity.department = department;
        return employeeEntity;
    }
}
