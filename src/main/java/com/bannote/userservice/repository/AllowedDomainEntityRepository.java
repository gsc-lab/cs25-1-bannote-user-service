package com.bannote.userservice.repository;

import com.bannote.userservice.entity.AllowedDomainEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AllowedDomainEntityRepository extends JpaRepository<AllowedDomainEntity, Long> {

    Boolean existsByDomain(String domain);
    Optional<AllowedDomainEntity> getByDomain(String domain);
}