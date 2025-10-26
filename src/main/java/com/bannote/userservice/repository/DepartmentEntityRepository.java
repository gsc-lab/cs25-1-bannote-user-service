package com.bannote.userservice.repository;

import com.bannote.userservice.entity.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentEntityRepository extends JpaRepository<DepartmentEntity, Long> {

    Optional<DepartmentEntity> findByCode(String code);
    Boolean existsByCode(String code);
    Boolean existsByName(String name);
}