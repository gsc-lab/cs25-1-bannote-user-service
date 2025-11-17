package com.bannote.userservice.service.alloweddomain;

import com.bannote.userservice.domain.allowedDomain.AllowedDomain;
import com.bannote.userservice.domain.allowedDomain.field.Domain;
import com.bannote.userservice.entity.AllowedDomainEntity;
import com.bannote.userservice.exception.ErrorCode;
import com.bannote.userservice.exception.UserServiceException;
import com.bannote.userservice.proto.allowed_domain.v1.AddAllowedDomainRequest;
import com.bannote.userservice.proto.allowed_domain.v1.ListAllowedDomainRequest;
import com.bannote.userservice.proto.allowed_domain.v1.RemoveAllowedDomainRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AllowedDomainApplicationService {

    private final AllowedDomainCommandService commandService;
    private final AllowedDomainQueryService queryService;

    public AllowedDomain createAllowedDomain(AddAllowedDomainRequest request) {

        if (queryService.isAllowed(Domain.of(request.getDomain()))) {
            throw new UserServiceException(
                    ErrorCode.DUPLICATE_ALLOWED_DOMAIN,
                    String.format("Domain is already allowed {%s}", request.getDomain())
            );
        }

        AllowedDomain allowedDomain = AllowedDomain.create(Domain.of(request.getDomain()));

        return commandService.createAllowedDomain(allowedDomain);
    }

    public AllowedDomain deleteAllowedDomain(RemoveAllowedDomainRequest request) {

        return commandService.deleteAllowedDomain(Domain.of(request.getDomain()));
    }

    public Page<AllowedDomain> listAllowedDomains(ListAllowedDomainRequest request) {

        Page<AllowedDomainEntity> allowedDomainEntities = queryService.listAllowedDomains(
                request.getPage(),
                request.getSize()
        );

        return allowedDomainEntities.map(AllowedDomain::fromEntity);
    }
}
