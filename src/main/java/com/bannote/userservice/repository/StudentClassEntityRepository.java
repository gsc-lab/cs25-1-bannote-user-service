package com.bannote.userservice.repository;

import com.bannote.userservice.domain.studentclass.StudentClass;
import com.bannote.userservice.domain.studentclass.field.StudentClassStatus;
import com.bannote.userservice.entity.DepartmentEntity;
import com.bannote.userservice.entity.StudentClassEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentClassEntityRepository extends JpaRepository<StudentClassEntity, Long> {

    Optional<StudentClassEntity> findByCode(String code);
    boolean existsByCode(String code);

    boolean existsByNameAndDepartment(String name, DepartmentEntity department);

    @Query("SELECT sc FROM StudentClassEntity sc WHERE " +
            "(:department IS NULL OR sc.department = :department) AND " +
            "(:status IS NULL OR sc.status = :status)")
    Page<StudentClassEntity> findAllByDepartmentAndStatus(
            @Param("department") DepartmentEntity department,
            @Param("status") StudentClassStatus status,
            Pageable pageable
    );

    List<StudentClassEntity> findAllByCodeIn(List<String> codes);
}