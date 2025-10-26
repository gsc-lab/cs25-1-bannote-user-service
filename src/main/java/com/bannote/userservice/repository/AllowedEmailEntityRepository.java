package com.bannote.userservice.repository;

import com.bannote.userservice.entity.AllowedEmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllowedEmailEntityRepository extends JpaRepository<AllowedEmailEntity, Long> {

    Boolean existsByEmail(String email);
}