package com.bannote.userservice.repository;

import com.bannote.userservice.domain.user.field.UserStatus;
import com.bannote.userservice.domain.user.field.UserType;
import com.bannote.userservice.entity.DepartmentEntity;
import com.bannote.userservice.entity.StudentClassEntity;
import com.bannote.userservice.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("""
        SELECT u FROM UserEntity u
        LEFT JOIN FETCH u.student s
        LEFT JOIN FETCH s.studentClass sc
        LEFT JOIN FETCH sc.department d
        LEFT JOIN FETCH u.roles r
        WHERE u.code = :code
        """)
    Optional<UserEntity> findUserDetailByCode(String code);

    Boolean existsByEmail(String email);
    Boolean existsByCode(String studentCode);

    @Query("""
        SELECT DISTINCT u FROM UserEntity u
        LEFT JOIN FETCH u.student s
        LEFT JOIN FETCH s.studentClass sc
        LEFT JOIN FETCH sc.department d
        LEFT JOIN FETCH u.roles r
        WHERE (:userType IS NULL OR u.type = :userType)
        AND (:status IS NULL OR u.status = :status)
        AND (:studentClass IS NULL OR sc = :studentClass)
        AND (:department IS NULL OR d = :department)
        """)
    Page<UserEntity> findAllUserDetailByStudentClassAndTypeAndStatus(
            @Param("userType") UserType userType,
            @Param("status") UserStatus status,
            @Param("studentClass") StudentClassEntity studentClass,
            @Param("department") DepartmentEntity department,
            Pageable pageable
    );

    Optional<UserEntity> findByCode(String code);

    /**
     * 이름으로 사용자 검색 (초성 검색 지원)
     * MySQL REGEXP를 사용하여 "김ㅅ" 검색 시 "김서동", "김수진" 등 매칭
     * @param namePattern MySQL REGEXP 패턴 (KoreanSearchUtils.toNameSearchPattern() 사용)
     * @param pageable 페이징 정보
     * @return 검색된 사용자 목록
     */
    @Query(value = """
        SELECT * FROM `user` u
        WHERE u.deleted_at IS NULL
        AND u.name REGEXP :namePattern
        """, nativeQuery = true)
    Page<UserEntity> searchByName(
            @Param("namePattern") String namePattern,
            Pageable pageable);
}
