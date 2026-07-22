package com.bannote.userservice.service.alloweddomain;

import com.bannote.userservice.domain.allowedDomain.field.Domain;
import com.bannote.userservice.domain.user.field.UserEmail;
import com.bannote.userservice.entity.AllowedDomainEntity;
import com.bannote.userservice.exception.ErrorCode;
import com.bannote.userservice.exception.UserServiceException;
import com.bannote.userservice.repository.AllowedDomainEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.retrytopic.RetryTopicConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AllowedDomainQueryService {

    private final AllowedDomainEntityRepository repository;

    /**
     * 이메일 도메인이 허용된 도메인인지 확인
     * @param email UserEmail 객체
     * @return  허용된 도메인이면 true, 아니면 false
     */
    public Boolean isAllowed(UserEmail email) {

        return isAllowed(Domain.of(email.getDomain()));
    }

    public Boolean isAllowed(Domain domain) {
        return repository.existsByDomain(domain.getValue());
    }

    public AllowedDomainEntity getAllowedDomainEntityByDomain(Domain domain) {

        return repository.getByDomain(domain.getValue())
                .orElseThrow(() -> new UserServiceException(
                        ErrorCode.ALLOWED_DOMAIN_NOT_FOUND,
                        String.format("Domain not found (%s)", domain.getValue())
                ));
    }

    public Page<AllowedDomainEntity> listAllowedDomains(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable);
    }
}
