package com.bannote.userservice.service.studentclass;

import com.bannote.userservice.domain.department.field.DepartmentCode;
import com.bannote.userservice.domain.studentclass.field.StudentClassCode;
import com.bannote.userservice.entity.StudentClassEntity;
import com.bannote.userservice.exception.ErrorCode;
import com.bannote.userservice.exception.UserServiceException;
import com.bannote.userservice.repository.StudentClassEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentClassQueryService {

    private final StudentClassEntityRepository studentClassEntityRepository;

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

}
