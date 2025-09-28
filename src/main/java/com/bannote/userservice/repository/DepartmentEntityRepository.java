package com.bannote.userservice.repository;

import com.bannote.userservice.entity.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentEntityRepository extends JpaRepository<DepartmentEntity, Long> {
}