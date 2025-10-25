package com.bannote.userservice.repository;

import com.bannote.userservice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {

    @Query("""
        SELECT u FROM UserEntity u
        LEFT JOIN FETCH u.student s
        LEFT JOIN FETCH s.studentClass sc
        LEFT JOIN FETCH sc.department d
        LEFT JOIN FETCH u.roles r
        WHERE u.email = :email
        """)
    Optional<UserEntity> findUserDetailByEmail(String email);

    Boolean existsByEmail(String email);
    Boolean existsByCode(String studentCode);
}
