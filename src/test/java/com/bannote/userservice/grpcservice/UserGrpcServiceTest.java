package com.bannote.userservice.grpcservice;

import com.bannote.userservice.domain.studentclass.field.StudentClassStatus;
import com.bannote.userservice.entity.DepartmentEntity;
import com.bannote.userservice.entity.StudentClassEntity;
import com.bannote.userservice.entity.UserEntity;
import com.bannote.userservice.proto.common.v1.UserType;
import com.bannote.userservice.proto.user.v1.CreateUserRequest;
import com.bannote.userservice.proto.user.v1.CreateUserResponse;
import com.bannote.userservice.repository.DepartmentEntityRepository;
import com.bannote.userservice.repository.StudentClassEntityRepository;
import com.bannote.userservice.repository.UserEntityRepository;
import io.grpc.internal.testing.StreamRecorder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Year;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class UserGrpcServiceTest {

    @Autowired
    private UserGrpcService userGrpcService;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private StudentClassEntityRepository studentClassEntityRepository;

    @Autowired
    private DepartmentEntityRepository departmentEntityRepository;

    private DepartmentEntity testDepartment;
    private StudentClassEntity testStudentClass;

    private final String DEPARTMENT_CODE = "DEPT001";
    private final String STUDENT_CLASS_CODE = "0000000";

    @BeforeEach
    void setUp() {
        if (departmentEntityRepository.findByCode(DEPARTMENT_CODE).isEmpty()
                && studentClassEntityRepository.findByCode(STUDENT_CLASS_CODE).isEmpty()) {
            // 테스트용 학과 생성 및 저장
            testDepartment = DepartmentEntity.create(
                    DEPARTMENT_CODE,
                    "글로벌시스템융합과",
                    null
            );
            departmentEntityRepository.save(testDepartment);

            // 테스트용 학급 생성 및 저장
            testStudentClass = StudentClassEntity.create(
                    testDepartment,
                    STUDENT_CLASS_CODE,
                    "2024 A반",
                    Year.of(2024),
                    Year.of(2027),
                    StudentClassStatus.ACTIVE,
                    null
            );
            studentClassEntityRepository.save(testStudentClass);
        }
    }

    @Test
    @DisplayName("학생 회원가입 gRPC 요청 시 UserEntity, StudentEntity, UserRoleEntity가 DB에 저장된다")
    void createUser_studentType_shouldSaveAllEntitiesSuccessfully() throws Exception {
        // Given - gRPC CreateUserRequest 생성
        CreateUserRequest request = CreateUserRequest.newBuilder()
                .setUserCode("2024001")
                .setUserEmail("student@yju.ac.kr")
                .setFamilyName("김")
                .setGivenName("철수")
                .setUserType(UserType.USER_TYPE_STUDENT)
                .setProfileImageUrl("https://example.com/profile.jpg")
                .setStudentClassCode(STUDENT_CLASS_CODE)
                .setDepartmentCode(DEPARTMENT_CODE)
                .build();

        // When - gRPC 서비스 호출
        StreamRecorder<CreateUserResponse> responseObserver = StreamRecorder.create();
        userGrpcService.createUser(request, responseObserver);

        // Then - 응답 대기 및 검증
        assertThat(responseObserver.awaitCompletion(5, TimeUnit.SECONDS)).isTrue();

        List<CreateUserResponse> results = responseObserver.getValues();
        assertThat(results).hasSize(1);

        CreateUserResponse response = results.get(0);
        assertThat(response.getSuccess()).isTrue();
        assertThat(response.getCanLogin()).isFalse();
        assertThat(response.hasUser()).isTrue();

        // 반환된 User 정보 검증
        assertThat(response.getUser().getUserCode()).isEqualTo("2024001");
        assertThat(response.getUser().getUserEmail()).isEqualTo("student@yju.ac.kr");
        assertThat(response.getUser().getFamilyName()).isEqualTo("김");
        assertThat(response.getUser().getGivenName()).isEqualTo("철수");
        assertThat(response.getUser().getUserType()).isEqualTo(UserType.USER_TYPE_STUDENT);
        assertThat(response.getUser().getProfileImageUrl()).isEqualTo("https://example.com/profile.jpg");

        // 학생 정보 검증
        assertThat(response.getUser().hasStudentClassCode()).isTrue();
        assertThat(response.getUser().getStudentClassCode()).isEqualTo(STUDENT_CLASS_CODE);
        assertThat(response.getUser().getStudentClassName()).isEqualTo("2024 A반");
        assertThat(response.getUser().getDepartmentCode()).isEqualTo(DEPARTMENT_CODE);
        assertThat(response.getUser().getDepartmentName()).isEqualTo("글로벌시스템융합과");

        // DB에 실제로 저장되었는지 검증
        UserEntity savedUser = userEntityRepository.findUserDetailByEmail("student@yju.ac.kr").orElseThrow();
        assertThat(savedUser.getCode()).isEqualTo("2024001");
        assertThat(savedUser.getStudent()).isNotNull();
        assertThat(savedUser.getStudent().getStudentClass().getCode()).isEqualTo(STUDENT_CLASS_CODE);
        assertThat(savedUser.getRoles()).hasSize(2); // DEFAULT + STUDENT
    }

    @Test
    @DisplayName("중복된 이메일로 회원가입 시 실패 응답 반환")
    void createUser_withDuplicateEmail_shouldReturnFailureResponse() throws Exception {
        // Given - 먼저 사용자 생성
        CreateUserRequest firstRequest = CreateUserRequest.newBuilder()
                .setUserCode("2024002")
                .setUserEmail("duplicate@yju.ac.kr")
                .setFamilyName("이")
                .setGivenName("영희")
                .setUserType(UserType.USER_TYPE_STUDENT)
                .setStudentClassCode(STUDENT_CLASS_CODE)
                .setDepartmentCode(DEPARTMENT_CODE)
                .build();

        StreamRecorder<CreateUserResponse> firstObserver = StreamRecorder.create();
        userGrpcService.createUser(firstRequest, firstObserver);
        firstObserver.awaitCompletion(5, TimeUnit.SECONDS);

        // 같은 이메일로 다시 회원가입 시도
        CreateUserRequest request = CreateUserRequest.newBuilder()
                .setUserCode("2024003")
                .setUserEmail("duplicate@yju.ac.kr")
                .setFamilyName("박")
                .setGivenName("민수")
                .setUserType(UserType.USER_TYPE_STUDENT)
                .setStudentClassCode(STUDENT_CLASS_CODE)
                .setDepartmentCode(DEPARTMENT_CODE)
                .build();

        // When
        StreamRecorder<CreateUserResponse> responseObserver = StreamRecorder.create();
        userGrpcService.createUser(request, responseObserver);

        // Then
        assertThat(responseObserver.awaitCompletion(5, TimeUnit.SECONDS)).isTrue();

        List<CreateUserResponse> results = responseObserver.getValues();
        CreateUserResponse response = results.get(0);

        assertThat(response.getSuccess()).isFalse();
        assertThat(response.getCanLogin()).isFalse();
        assertThat(response.hasReason()).isTrue();
        assertThat(response.getReason()).contains("Email already exists");
    }

    @Test
    @DisplayName("중복된 학번으로 회원가입 시 실패 응답 반환")
    void createUser_withDuplicateCode_shouldReturnFailureResponse() throws Exception {
        // Given - 먼저 사용자 생성
        CreateUserRequest firstRequest = CreateUserRequest.newBuilder()
                .setUserCode("2024004")
                .setUserEmail("first@yju.ac.kr")
                .setFamilyName("최")
                .setGivenName("민지")
                .setUserType(UserType.USER_TYPE_STUDENT)
                .setStudentClassCode(STUDENT_CLASS_CODE)
                .setDepartmentCode(DEPARTMENT_CODE)
                .build();

        StreamRecorder<CreateUserResponse> firstObserver = StreamRecorder.create();
        userGrpcService.createUser(firstRequest, firstObserver);
        firstObserver.awaitCompletion(5, TimeUnit.SECONDS);

        // 같은 학번으로 다시 회원가입 시도
        CreateUserRequest request = CreateUserRequest.newBuilder()
                .setUserCode("2024004")
                .setUserEmail("test@yju.ac.kr")
                .setFamilyName("정")
                .setGivenName("하늘")
                .setUserType(UserType.USER_TYPE_STUDENT)
                .setStudentClassCode(STUDENT_CLASS_CODE)
                .setDepartmentCode(DEPARTMENT_CODE)
                .build();

        // When
        StreamRecorder<CreateUserResponse> responseObserver = StreamRecorder.create();
        userGrpcService.createUser(request, responseObserver);

        // Then
        assertThat(responseObserver.awaitCompletion(5, TimeUnit.SECONDS)).isTrue();

        List<CreateUserResponse> results = responseObserver.getValues();
        CreateUserResponse response = results.get(0);

        assertThat(response.getSuccess()).isFalse();
        assertThat(response.getCanLogin()).isFalse();
        assertThat(response.hasReason()).isTrue();
        assertThat(response.getReason()).contains("User code already exists");
    }

    @Test
    @DisplayName("gRPC 요청의 모든 필드가 올바르게 엔티티에 매핑된다")
    void createUser_shouldMapAllRequestFieldsCorrectly() throws Exception {
        // Given - 모든 필드를 채운 요청
        CreateUserRequest request = CreateUserRequest.newBuilder()
                .setUserCode("2024006")
                .setUserEmail("complete@yju.ac.kr")
                .setFamilyName("최")
                .setGivenName("수진")
                .setUserType(UserType.USER_TYPE_STUDENT)
                .setProfileImageUrl("https://example.com/avatar.png")
                .setStudentClassCode(STUDENT_CLASS_CODE)
                .setDepartmentCode(DEPARTMENT_CODE)
                .build();

        // When
        StreamRecorder<CreateUserResponse> responseObserver = StreamRecorder.create();
        userGrpcService.createUser(request, responseObserver);

        // Then
        assertThat(responseObserver.awaitCompletion(5, TimeUnit.SECONDS)).isTrue();

        List<CreateUserResponse> results = responseObserver.getValues();
        CreateUserResponse response = results.get(0);

        // Request → Response 필드 매핑 검증
        assertThat(response.getSuccess()).isTrue();
        assertThat(response.getUser().getUserCode()).isEqualTo(request.getUserCode());
        assertThat(response.getUser().getUserEmail()).isEqualTo(request.getUserEmail());
        assertThat(response.getUser().getFamilyName()).isEqualTo(request.getFamilyName());
        assertThat(response.getUser().getGivenName()).isEqualTo(request.getGivenName());
        assertThat(response.getUser().getUserType()).isEqualTo(request.getUserType());
        assertThat(response.getUser().getProfileImageUrl()).isEqualTo(request.getProfileImageUrl());
        assertThat(response.getUser().getStudentClassCode()).isEqualTo(request.getStudentClassCode());
    }
}