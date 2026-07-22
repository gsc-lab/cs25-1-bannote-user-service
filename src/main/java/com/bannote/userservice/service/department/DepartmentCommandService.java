package com.bannote.userservice.service.department;

import com.bannote.userservice.context.AuthorizationUtil;
import com.bannote.userservice.domain.department.Department;
import com.bannote.userservice.domain.department.field.DepartmentCode;
import com.bannote.userservice.domain.department.field.DepartmentName;
import com.bannote.userservice.entity.DepartmentEntity;
import com.bannote.userservice.exception.ErrorCode;
import com.bannote.userservice.exception.UserServiceException;
import com.bannote.userservice.repository.DepartmentEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
@Transactional
@RequiredArgsConstructor
public class DepartmentCommandService {

    private final DepartmentQueryService departmentQueryService;
    private final DepartmentEntityRepository departmentEntityRepository;

    /**
     * 학과 생성
     * @param department 생성할 학과 도메인 객체
     * @return 생성된 학과 도메인 객체
     */
    public Department createDepartment(Department department) {

        validateDuplicateCode(department.getDepartmentCode());
        validateDuplicateName(department.getDepartmentName(), null);

        DepartmentEntity departmentEntity = DepartmentEntity.create(
                department.getDepartmentCode().getValue(),
                department.getDepartmentName().getValue(),
                AuthorizationUtil.getCurrentAuthInfo().userCode().getValue()
        );

        DepartmentEntity saved = departmentEntityRepository.save(departmentEntity);

        return Department.fromEntity(saved);

    }

    /**
     * 학과 정보 수정
     * @param department 수정할 학과 도메인 객체
     * @return 수정된 학과 도메인 객체
     */
    public Department updateDepartment(Department department) {

        DepartmentEntity departmentEntity = departmentQueryService.getDepartmentEntityByCode(
                department.getDepartmentCode().getValue()
        );

        // 이름 수정
        if (department.getDepartmentName() != null) {
            validateDuplicateName(department.getDepartmentName(), departmentEntity.getName());
            departmentEntity.setName(department.getDepartmentName().getValue());
        }

        return Department.fromEntity(departmentEntity);
    }

    /**
     * 학과 삭제
     * @param code 학과 코드
     * @return 삭제된 학과 도메인 객체
     */
    public Department deleteDepartment(DepartmentCode code) {
        DepartmentEntity departmentEntity = departmentQueryService.getDepartmentEntityByCode(
                code.getValue()
        );

        departmentEntity.setDeletedBy(AuthorizationUtil.getCurrentAuthInfo().userCode().getValue());
        departmentEntity.setDeletedAt(Timestamp.from(java.time.Instant.now()));

        return Department.fromEntity(departmentEntity);
    }

    /**
     * 학과 코드 중복 검증
     * @param code 학과 코드
     */
    private void validateDuplicateCode(DepartmentCode code) {
        if (departmentQueryService.existsByCode(code)) {
            throw new UserServiceException(
                    ErrorCode.DUPLICATE_DEPARTMENT_CODE,
                    String.format("Department code already exists: %s", code.getValue())
            );
        }
    }

    /**
     * 학과 이름 중복 검증
     * @param newName 수정할 학과 이름
     * @param currentName 현재 학과 이름
     */
    private void validateDuplicateName(DepartmentName newName, String currentName) {
        if (newName.getValue().equals(currentName)) {
            return;
        }

        if (departmentQueryService.existsByName(newName)) {
            throw new UserServiceException(
                    ErrorCode.DUPLICATE_DEPARTMENT_NAME,
                    String.format("Department name already exists: %s", newName.getValue())
            );
        }

    }

}
