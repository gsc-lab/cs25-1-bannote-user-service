package com.bannote.userservice.service.studentclass;

import com.bannote.userservice.domain.department.Department;
import com.bannote.userservice.domain.studentclass.StudentClass;
import com.bannote.userservice.domain.studentclass.field.StudentClassCode;
import com.bannote.userservice.domain.studentclass.field.StudentClassName;
import com.bannote.userservice.domain.studentclass.field.StudentClassStatus;
import com.bannote.userservice.entity.DepartmentEntity;
import com.bannote.userservice.entity.StudentClassEntity;
import com.bannote.userservice.proto.student_class.v1.*;
import com.bannote.userservice.service.department.DepartmentQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentClassApplicationService {

    private final StudentClassCommandService studentClassCommandService;
    private final StudentClassQueryService studentClassQueryService;
    private final DepartmentQueryService departmentQueryService;

    public StudentClass getStudentClass(GetStudentClassRequest request) {

        return studentClassQueryService.getStudentClassByCode(request.getStudentClassCode());
    }

    public StudentClass createStudentClass(CreateStudentClassRequest request) {

        DepartmentEntity departmentEntity = departmentQueryService.getDepartmentEntityByCode(request.getDepartmentCode());

        StudentClass studentClass = StudentClass.create(
                Department.fromEntity(departmentEntity),
                StudentClassCode.of(request.getStudentClassCode()),
                StudentClassName.of(request.getStudentClassName()),
                Year.of(request.getAdmissionYear()),
                Year.of(request.getGraduationYear()),
                StudentClassStatus.ACTIVE,  // 이미 졸업한 반을 작성하는 경우에는 생성 후 별도 처리 필요
                null
        );

        return studentClassCommandService.createStudentClass(studentClass, departmentEntity);
    }

    public StudentClass updateStudentClass(UpdateStudentClassRequest request) {

        StudentClass studentClass = StudentClass.update(
                StudentClassCode.of(request.getStudentClassCode()),
                request.hasStudentClassName() ? StudentClassName.of(request.getStudentClassName()) : null,
                request.hasAdmissionYear() ? Year.of(request.getAdmissionYear()) : null,
                request.hasGraduationYear() ? Year.of(request.getGraduationYear()) : null,
                request.hasStatus() ? StudentClassStatus.of(request.getStatus()) : null
        );

        return studentClassCommandService.updateStudentClass(studentClass);
    }

    public StudentClass deleteStudentClass(DeleteStudentClassRequest request) {

        return studentClassCommandService.deleteStudentClass(StudentClassCode.of(request.getStudentClassCode()));
    }

    /**
     * 학반 목록 페이징 조회
     * @param request
     * @return
     */
    public Page<StudentClass> listStudentClasses(ListStudentClassesRequest request) {

        Page<StudentClassEntity> studentClassEntityPage = studentClassQueryService.listStudentClasses(
                request.hasDepartmentCode()
                        ? departmentQueryService.getDepartmentEntityByCode(request.getDepartmentCode())
                        : null,
                request.hasStatus() ? StudentClassStatus.of(request.getStatus()) : null,
                request.getPage(),
                request.getSize()
        );

        return studentClassEntityPage.map(StudentClass::fromEntity);
    }
}
