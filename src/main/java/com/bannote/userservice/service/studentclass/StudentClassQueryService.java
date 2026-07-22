package com.bannote.userservice.service.studentclass;

import com.bannote.userservice.domain.studentclass.StudentClass;
import com.bannote.userservice.domain.studentclass.field.StudentClassCode;
import com.bannote.userservice.domain.studentclass.field.StudentClassName;
import com.bannote.userservice.domain.studentclass.field.StudentClassStatus;
import com.bannote.userservice.entity.DepartmentEntity;
import com.bannote.userservice.entity.StudentClassEntity;
import com.bannote.userservice.exception.ErrorCode;
import com.bannote.userservice.exception.UserServiceException;
import com.bannote.userservice.repository.StudentClassEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentClassQueryService {

    private final StudentClassEntityRepository studentClassEntityRepository;

    /**
     * 학반 코드를 통해 학반 엔티티 조회
     * @param code 학빈 코드
     * @return 학반 엔티티
     * @throws UserServiceException 해당 코드를 가진 학반이 존재하지 않는 경우
     */
    public StudentClassEntity getStudentClassEntityByCode(String code) {

        StudentClassCode studentClassCode = StudentClassCode.of(code);

        return studentClassEntityRepository.findByCode(studentClassCode.getValue())
                .orElseThrow(
                        () -> new UserServiceException(
                                ErrorCode.STUDENT_CLASS_NOT_FOUND,
                                "Student Class not found with code: " + code
                        )
                );
    }

    /**
     * 학급 코드로 학급 도메인 조회
     * @param code - 학급 코드 (String)
     * @return 조회된 StudentClass 객체
     */
    public StudentClass getStudentClassByCode(String code) {

        StudentClassCode.validate(code);

        return StudentClass.fromEntity(studentClassEntityRepository.findByCode(code)
                .orElseThrow(() -> new UserServiceException(
                                ErrorCode.STUDENT_CLASS_NOT_FOUND,
                                "Student Class not found with code: " + code
                        )
                )
        );
    }

    /**
     * 동일한 코드의 학반이 있는지 확인
     * @param code 학반 코드
     * @return 존재할 경우 True, 존재 하지 않은 경우 False
     */
    public boolean existsByCode(StudentClassCode code) {

        return studentClassEntityRepository.existsByCode(code.getValue());
    }

    /**
     * 동일 학과 내에 같은 이름의 학반이 있는지 확인
     * @param name 학반 이름
     * @param departmentEntity 학과 엔티티
     * @param studentClassEntity 학반 엔티티
     * @return 존재할 경우 True, 존재 하지 않은 경우 False
     */
    public boolean existsByNameAndDepartment(StudentClassName name, DepartmentEntity departmentEntity, StudentClassEntity studentClassEntity) {

        if (studentClassEntity != null && name.getValue().equals(studentClassEntity.getName())) {
            return false;
        }

        return studentClassEntityRepository.existsByNameAndDepartment(name.getValue(), departmentEntity);
    }

    /**
     * 학과의 학반 목록 페이징 조회
     * @param departmentEntity 학과 엔티티
     * @param status 학반 상태
     * @param page 페이지 번호 (0부터 시작)
     * @param size 페이지 크기
     * @return 페이징된 학반 엔티티 목록
     */
    public Page<StudentClassEntity> listStudentClasses(DepartmentEntity departmentEntity, StudentClassStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return studentClassEntityRepository.findAllByDepartmentAndStatus(departmentEntity, status, pageable);
    }

    public List<StudentClass> getManyStudentClasses(List<StudentClassCode> studentClassCodeList) {

        List<String> codes = studentClassCodeList.stream()
                .map(StudentClassCode::getValue)
                .toList();

        return studentClassEntityRepository.findAllByCodeIn(codes).stream()
                .map(StudentClass::fromEntity)
                .toList();
    }
}
