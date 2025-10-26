package com.bannote.userservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.sql.Timestamp;
import java.time.Instant;

@Getter
@Entity
@NoArgsConstructor
@DynamicUpdate
@SQLRestriction("deleted_at IS NULL")
@Table(name = "department", indexes = {
        @Index(name = "idx_departmententity_name_unq", columnList = "name", unique = true)
})
@SQLDelete(sql = "UPDATE department SET deleted_at = NOW() WHERE id = ?")
public class DepartmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Setter
    @Column(name = "name", unique = true, nullable = false)
    private String name;

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

    public static DepartmentEntity create(
            String code,
            String name,
            String createdBy
    ) {
        DepartmentEntity departmentEntity = new DepartmentEntity();
        departmentEntity.code = code;
        departmentEntity.name = name;
        departmentEntity.createdBy = createdBy;
        return departmentEntity;
    }

}
