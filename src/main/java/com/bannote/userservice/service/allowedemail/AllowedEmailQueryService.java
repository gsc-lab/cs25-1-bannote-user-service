package com.bannote.userservice.service.allowedemail;

import com.bannote.userservice.domain.user.field.UserEmail;
import com.bannote.userservice.repository.AllowedEmailEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AllowedEmailQueryService {

    private final AllowedEmailEntityRepository allowedEmailEntityRepository;

    /**
     * 이메일 도메인이 허용된 도메인인지 확인
     * @param email UserEmail 객체
     * @return  허용된 도메인이면 true, 아니면 false
     */
    public Boolean isAllowed(UserEmail email) {
        return allowedEmailEntityRepository.existsByEmail(email.getDomain());
    }
}
