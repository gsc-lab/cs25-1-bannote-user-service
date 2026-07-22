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
import java.time.Year;

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
        validateDuplicateName(studentClass.getStudentClassName(), departmentEntity, null);

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

    public StudentClass updateStudentClass(StudentClass studentClass) {

        StudentClassEntity studentClassEntity = studentClassQueryService.getStudentClassEntityByCode(
                studentClass.getStudentClassCode().getValue()
        );

        // TODO 학과 변경 추가 검토

        if (studentClass.getStudentClassName() != null) {
            validateDuplicateName(studentClass.getStudentClassName(), studentClassEntity);
            studentClassEntity.setName(studentClass.getStudentClassName().getValue());
        }

        Year admissionYear = studentClass.getAdmissionYear() != null ?
                studentClass.getAdmissionYear() : studentClassEntity.getAdmissionYear();
        Year graduationYear = studentClass.getGraduationYear() != null ?
                studentClass.getGraduationYear() : studentClassEntity.getGraduationYear();

        validateYearRange(admissionYear, graduationYear);

        if (studentClass.getAdmissionYear() != null) {
            studentClassEntity.setAdmissionYear(admissionYear);
        }
        if (studentClass.getGraduationYear() != null) {
            studentClassEntity.setGraduationYear(graduationYear);
        }

        if (studentClass.getStatus() != null) {
            studentClassEntity.setStatus(studentClass.getStatus());
        }

        return StudentClass.fromEntity(studentClassEntity);
    }

    /**
     * 동일 학과 내에서 학반 이름 중복 검증
     * @param name 학반 이름
     * @param studentClassEntity 학반 엔티티
     */
    private void validateDuplicateName(StudentClassName name, StudentClassEntity studentClassEntity) {
        validateDuplicateName(name, studentClassEntity.getDepartment(), studentClassEntity);
    }

    private void validateDuplicateName(StudentClassName name, DepartmentEntity departmentEntity, StudentClassEntity studentClassEntity) {
        if (studentClassQueryService.existsByNameAndDepartment(name, departmentEntity, studentClassEntity)) {
            throw new UserServiceException(
                    ErrorCode.DUPLICATE_STUDENT_CLASS_NAME,
                    String.format("Student class name '%s' already exists in department '%s'",
                            name.getValue(), departmentEntity.getName())
            );
        }
    }

    /**
     * 입학연도와 졸업연도 범위 검증
     * @param admissionYear 입학연도
     * @param graduationYear 졸업연도
     */
    private void validateYearRange(Year admissionYear, Year graduationYear) {
        if (admissionYear.isAfter(graduationYear) || admissionYear.equals(graduationYear)) {
            throw new UserServiceException(
                    ErrorCode.INVALID_ARGUMENT,
                    String.format("Admission year (%d) must be before graduation year (%d)",
                            admissionYear.getValue(), graduationYear.getValue())
            );
        }
    }
}
