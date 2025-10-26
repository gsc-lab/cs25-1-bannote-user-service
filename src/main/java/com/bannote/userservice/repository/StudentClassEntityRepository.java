package com.bannote.userservice.repository;

import com.bannote.userservice.entity.StudentClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentClassEntityRepository extends JpaRepository<StudentClassEntity, Long> {

    Optional<StudentClassEntity> findByCode(String code);
}