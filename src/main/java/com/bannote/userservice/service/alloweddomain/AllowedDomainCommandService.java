package com.bannote.userservice.service.alloweddomain;

import com.bannote.userservice.context.AuthorizationUtil;
import com.bannote.userservice.domain.allowedDomain.AllowedDomain;
import com.bannote.userservice.domain.allowedDomain.field.Domain;
import com.bannote.userservice.entity.AllowedDomainEntity;
import com.bannote.userservice.repository.AllowedDomainEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AllowedDomainCommandService {

    private final AllowedDomainEntityRepository repository;
    private final AllowedDomainQueryService queryService;

    public AllowedDomain createAllowedDomain(AllowedDomain allowedDomain) {

        AllowedDomainEntity entity = AllowedDomainEntity.create(
                allowedDomain.getDomain().getValue(),
                AuthorizationUtil.getCurrentAuthInfo().userCode().getValue()
        );

        return AllowedDomain.fromEntity(repository.save(entity));
    }

    public AllowedDomain deleteAllowedDomain(Domain domain) {

        AllowedDomainEntity entity = queryService.getAllowedDomainEntityByDomain(domain);

        repository.delete(entity);

        return AllowedDomain.fromEntity(entity);
    }
}
