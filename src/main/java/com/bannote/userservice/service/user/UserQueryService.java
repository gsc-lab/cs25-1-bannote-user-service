package com.bannote.userservice.service.user;

import com.bannote.userservice.domain.user.Employee;
import com.bannote.userservice.domain.user.Student;
import com.bannote.userservice.domain.user.UserBasic;
import com.bannote.userservice.domain.user.UserDetail;
import com.bannote.userservice.domain.user.field.UserCode;
import com.bannote.userservice.domain.user.field.UserEmail;
import com.bannote.userservice.entity.UserEntity;
import com.bannote.userservice.exception.ErrorCode;
import com.bannote.userservice.exception.UserServiceException;
import com.bannote.userservice.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {

    private final UserEntityRepository userEntityRepository;

    /**
     * 이메일로 유저 상세 정보 조회
     * @param email 이메일
     * @return  UserDetail Optional 객체
     */
    public Optional<UserDetail> findUserDetailByEmail(String email) {
        return userEntityRepository.findUserDetailByEmail(email)
                .map(this::toUserDetail);
    }

    /**
     * 이메일로 유저 존재 여부 확인
     * @param email UserEmail 객체
     * @return  존재하면 true, 없으면 false
     */
    public Boolean existsByEmail(UserEmail email) {
        return userEntityRepository.existsByEmail(email.getValue());
    }

    /**
     * 학번으로 유저 존재 여부 확인
     * @param code UserCode 객체
     * @return  존재하면 true, 없으면 false
     */
    public Boolean existsByCode(UserCode code) {
        return userEntityRepository.existsByCode(code.getValue());
    }

    /**
     * UserEntity를 UserDetail로 변환
     */
    private UserDetail toUserDetail(UserEntity userEntity) {
        return switch (userEntity.getType()) {
            case STUDENT -> UserDetail.ofStudent(Student.fromEntity(userEntity));
            case EMPLOYEE -> UserDetail.ofEmployee(Employee.fromEntity(userEntity));
            case SERVICE, OTHER -> UserDetail.ofBasic(UserBasic.fromEntity(userEntity));
            default -> throw new UserServiceException(ErrorCode.ENTITY_TO_DOMAIN_CONVERSION_FAILED,
                    String.format("Unknown user type: %s", userEntity.getType())
            );
        };
    }

}
