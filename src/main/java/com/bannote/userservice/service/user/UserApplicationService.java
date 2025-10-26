package com.bannote.userservice.service.user;

import com.bannote.userservice.domain.user.Employee;
import com.bannote.userservice.domain.user.Student;
import com.bannote.userservice.domain.user.UserBasic;
import com.bannote.userservice.domain.user.UserDetail;
import com.bannote.userservice.domain.user.field.*;
import com.bannote.userservice.entity.DepartmentEntity;
import com.bannote.userservice.entity.StudentClassEntity;
import com.bannote.userservice.exception.ErrorCode;
import com.bannote.userservice.exception.UserServiceException;
import com.bannote.userservice.proto.user.v1.CreateUserRequest;
import com.bannote.userservice.proto.user.v1.UserLoginResponse;
import com.bannote.userservice.service.allowedemail.AllowedEmailQueryService;
import com.bannote.userservice.service.department.DepartmentQueryService;
import com.bannote.userservice.service.studentclass.StudentClassQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserApplicationService {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;
    private final AllowedEmailQueryService allowedEmailQueryService;
    private final DepartmentQueryService departmentQueryService;
    private final StudentClassQueryService studentClassQueryService;

    /**
     * 유저 로그인 처리
     * @param email 이메일
     * @return UserLoginResponse 객체
     */
    public UserLoginResponse userLogin(String email) {

        return userQueryService.findUserDetailByEmail(email)
                .map(userDetail -> {
                    return UserLoginResponse.newBuilder()
                            .setExists(true)
                            .setCanLogin(userDetail.isLoginAllowed())
                            .setUser(userDetail.toProto())
                            .build();
                })
                .orElse(UserLoginResponse.newBuilder()
                        .setExists(false)
                        .setCanLogin(false)
                        .build());
    }

    /**
     * 기본 유저 생성
     * @param request CreateUserRequest 객체
     * @return 생성된 UserDetail 객체
     */
    public UserDetail createUser(CreateUserRequest request) {

        UserBasic userBasic = userCommandService.createUserBasic(buildUserBasic(request));

        return UserDetail.ofBasic(userBasic);
    }

    /**
     * 학생 유저 생성
     * @param request CreateUserRequest 객체
     * @return 생성된 UserDetail 객체
     */
    public UserDetail createStudent(CreateUserRequest request) {

        // 반과 학과가 올바르게 선택 되었는지 검증
        StudentClassEntity studentClassEntity = studentClassQueryService
                .getStudentClassEntityByCode(request.getStudentClassCode());

        // 반의 학과 코드와 요청의 학과 코드가 일치하는지 검증
        if (!studentClassEntity.getDepartment().getCode().equals(request.getDepartmentCode())) {
            throw new UserServiceException(
                    ErrorCode.STUDENT_CLASS_DEPARTMENT_MISMATCH,
                    "Student class department does not match with provided department code. StudentClassCode: "
                            + request.getStudentClassCode()
                            + ", DepartmentCode: "
                            + request.getDepartmentCode()
            );
        }

        // 유저 기본 정보 생성
        UserBasic userBasic = buildUserBasic(request);

        // 학생 생성 (유저 + 학생 정보)
        Student student = userCommandService.createStudent(userBasic, studentClassEntity);

        return UserDetail.ofStudent(student);
    }

    /**
     * 직원 유저 생성
     * @param request CreateUserRequest 객체
     * @return 생성된 UserDetail 객체
     */
    public UserDetail createEmployee(CreateUserRequest request) {

        // 학과가 올바르게 선택 되었는지 검증
        DepartmentEntity department = departmentQueryService.getDepartmentEntityByCode(request.getDepartmentCode());

        // 유저 기본 정보 생성
        UserBasic userBasic = buildUserBasic(request);

        // 직원 생성 (유저 + 직원 정보)
        Employee employee = userCommandService.createEmployee(userBasic, department);

        // UserDetail 생성 및 반환
        return UserDetail.ofEmployee(employee);
    }

    /**
     * CreateUserRequest로부터 UserBasic 객체 생성 (User 저장의 경우에 Entity를 생성하기 이전에 호출 하여 값을 검증)
     * @param request CreateUserRequest 객체
     * @return UserBasic 객체
     */
    private UserBasic buildUserBasic(CreateUserRequest request) {

        UserEmail email = UserEmail.of(request.getUserEmail());

        // 이메일 허용 여부 확인 (결과에 따라 유저 상태 결정)
        Boolean allowed = allowedEmailQueryService.isAllowed(email);

        return UserBasic.create(
                UserCode.of(request.getUserCode()),
                email,
                UserFamilyName.of(request.getFamilyName()),
                UserGivenName.of(request.getGivenName()),
                UserType.of(request.getUserType()),
                allowed ? UserStatus.ACTIVE : UserStatus.PENDING,
                UserProfileImage.of(request.getProfileImageUrl())
        );
    }

}
