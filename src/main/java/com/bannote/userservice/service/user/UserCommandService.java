package com.bannote.userservice.service.user;

import com.bannote.userservice.domain.user.Employee;
import com.bannote.userservice.domain.user.Student;
import com.bannote.userservice.domain.user.UserBasic;
import com.bannote.userservice.domain.user.field.UserRole;
import com.bannote.userservice.entity.*;
import com.bannote.userservice.exception.ErrorCode;
import com.bannote.userservice.exception.UserServiceException;
import com.bannote.userservice.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandService {

    private final UserEntityRepository userEntityRepository;
    private final UserQueryService userQueryService;

    /**
     * 기본 유저 생성
     * @param userBasic - 생성할 유저의 기본 정보
     * @return 생성된 UserBasic 객체
     * @throws UserServiceException 이메일 또는 학번이 중복될 경우 발생
     */
    public UserBasic createUserBasic(UserBasic userBasic) {

        UserEntity userEntity = createUserEntity(userBasic);

        // 기본 역할 추가
        userEntity.addRole(UserRole.DEFAULT, null);

        UserEntity saved = userEntityRepository.save(userEntity);

        return UserBasic.fromEntity(saved);
    }

    /**
     * 직원 유저 생성
     * @param userBasic - 생성할 직원 유저의 기본 정보
     * @param department - 소속 학과 정보
     * @return 생성된 Employee 객체
     * @throws UserServiceException 이메일 또는 학번이 중복될 경우 발생
     */
    public Employee createEmployee(UserBasic userBasic, DepartmentEntity department) {
        // UserEntity 생성
        UserEntity userEntity = createUserEntity(userBasic);

        // 기본 역할 추가 (직원은 DEFAULT만)
        userEntity.addRole(UserRole.DEFAULT, null);

        // EmployeeEntity 생성 및 연관관계 설정
        EmployeeEntity employeeEntity = EmployeeEntity.create(userEntity, department);
        userEntity.setEmployee(employeeEntity);

        // UserEntity 저장 (CascadeType.ALL로 EmployeeEntity도 함께 저장됨)
        UserEntity saved = userEntityRepository.save(userEntity);

        return Employee.fromEntity(saved);
    }

    /**
     * 학생 유저 생성
     * @param userBasic - 생성할 학생 유저의 기본 정보
     * @param studentClass - 소속 학급 정보
     * @return 생성된 Student 객체
     * @throws UserServiceException 이메일 또는 학번이 중복될 경우 발생
     */
    public Student createStudent(UserBasic userBasic, StudentClassEntity studentClass) {
        // UserEntity 생성
        UserEntity userEntity = createUserEntity(userBasic);

        // 학생 역할 추가 (DEFAULT + STUDENT)
        userEntity.addRole(UserRole.DEFAULT, null);
        userEntity.addRole(UserRole.STUDENT, null);

        // StudentEntity 생성 및 연관관계 설정
        StudentEntity studentEntity = StudentEntity.create(userEntity, studentClass);
        userEntity.setStudent(studentEntity);

        // UserEntity 저장 (CascadeType.ALL로 StudentEntity도 함께 저장됨)
        UserEntity saved = userEntityRepository.save(userEntity);

        return Student.fromEntity(saved);
    }

    /**
     * UserEntity 생성 (이메일 및 학번 중복 검사 포함)
     * @param userBasic - 생성할 유저의 기본 정보
     * @return 생성된 UserEntity 객체
     * @throws UserServiceException 이메일 또는 학번이 중복될 경우 발생
     */
    private UserEntity createUserEntity(UserBasic userBasic) {

        //  이메일 중복 검사
        if (userQueryService.existsByEmail(userBasic.getUserEmail())) {
            throw new UserServiceException(ErrorCode.DUPLICATE_EMAIL,
                    "Email already exists: " + userBasic.getUserEmail().getValue());
        }

        //  학번 중복 검사
        if (userQueryService.existsByCode(userBasic.getUserCode())) {
            throw new UserServiceException(ErrorCode.DUPLICATE_USER_CODE,
                    "User code already exists: " + userBasic.getUserCode().getValue());
        }

        return toUserEntity(userBasic);
    }

    /**
     * UserBasic을 UserEntity로 변환
     */
    private UserEntity toUserEntity(UserBasic userBasic) {
        return UserEntity.create(
                userBasic.getUserCode().getValue(),
                userBasic.getUserEmail().getValue(),
                userBasic.getUserName(),
                userBasic.getUserFamilyName().getValue(),
                userBasic.getUserGivenName().getValue(),
                userBasic.getUserType(),
                userBasic.getUserStatus(),
                userBasic.getUserBio().getValue(),
                userBasic.getUserProfileImage().getValue()
        );
    }

}
