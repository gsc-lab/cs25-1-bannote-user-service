package com.bannote.userservice.service.studentclass;

import com.bannote.userservice.context.AuthorizationUtil;
import com.bannote.userservice.domain.studentclass.StudentClass;
import com.bannote.userservice.domain.studentclass.field.StudentClassCode;
import com.bannote.userservice.domain.studentclass.field.StudentClassName;
import com.bannote.userservice.entity.DepartmentEntity;
import com.bannote.userservice.entity.StudentClassEntity;
import com.bannote.userservice.exception.ErrorCode;
import com.bannote.userservice.exception.UserServiceException;
import com.bannote.userservice.repository.StudentClassEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentClassCommandService {

    private final StudentClassQueryService studentClassQueryService;
    private final StudentClassEntityRepository studentClassEntityRepository;


    /**
     * 학반을 생성하는 함수
     * @param studentClass 학반 도메인
     * @param departmentEntity 학과 엔티티
     * @return 생성된 학반
     */
    public StudentClass createStudentClass(StudentClass studentClass, DepartmentEntity departmentEntity) {

        validateDuplicateCode(studentClass.getStudentClassCode());
        validateDuplicateName(studentClass.getStudentClassName(), departmentEntity);

        StudentClassEntity studentClassEntity = StudentClassEntity.create(
                departmentEntity,
                studentClass.getStudentClassCode().getValue(),
                studentClass.getStudentClassName().getValue(),
                studentClass.getAdmissionYear(),
                studentClass.getGraduationYear(),
                studentClass.getStatus(),
                AuthorizationUtil.getCurrentAuthInfo().userCode().getValue()
        );

        StudentClassEntity saved = studentClassEntityRepository.save(studentClassEntity);

        return StudentClass.fromEntity(saved);
    }

    /**
     * 학반 삭제 함수
     * @param code 학반 코드
     * @return 삭제된 학반
     */
    public StudentClass deleteStudentClass(StudentClassCode code) {

        StudentClassEntity studentClassEntity = studentClassQueryService.getStudentClassEntityByCode(code.getValue());

        studentClassEntity.setDeletedBy(AuthorizationUtil.getCurrentAuthInfo().userCode().getValue());
        studentClassEntity.setDeletedAt(Timestamp.from(java.time.Instant.now()));

        return StudentClass.fromEntity(studentClassEntity);
    }

    /**
     * 학반 코드 중복 검증
     * @param code 학반 코드
     */
    private void validateDuplicateCode(StudentClassCode code) {
        if (studentClassQueryService.existsByCode(code)) {
            throw new UserServiceException(
                    ErrorCode.DUPLICATE_STUDENT_CLASS_CODE,
                    String.format("Student class code already exists: %s", code.getValue())
            );
        }
    }

    /**
     * 동일 학과 내에서 학반 이름 중복 검증
     * @param name 학반 이름
     * @param departmentEntity 학과 엔티티
     */
    private void validateDuplicateName(StudentClassName name, DepartmentEntity departmentEntity) {
        if (studentClassQueryService.existsByNameAndDepartment(name, departmentEntity)) {
            throw new UserServiceException(
                    ErrorCode.DUPLICATE_STUDENT_CLASS_NAME,
                    String.format("Student class name '%s' already exists in department '%s'",
                            name.getValue(), departmentEntity.getName())
            );
        }
    }
}
