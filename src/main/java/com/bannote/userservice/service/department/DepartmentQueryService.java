package com.bannote.userservice.service.department;

import com.bannote.userservice.domain.department.Department;
import com.bannote.userservice.domain.department.field.DepartmentCode;
import com.bannote.userservice.entity.DepartmentEntity;
import com.bannote.userservice.exception.ErrorCode;
import com.bannote.userservice.exception.UserServiceException;
import com.bannote.userservice.repository.DepartmentEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DepartmentQueryService {

    private final DepartmentEntityRepository departmentEntityRepository;

    /**
     * 학과 코드로 학과 정보 조회
     * @param code - 학과 코드 (String)
     * @return  DepartmentEntity 객체
     * @throws UserServiceException 학과를 찾을 수 없을 때 발생
     */
    public DepartmentEntity getDepartmentEntityByCode(String code) throws UserServiceException {

        DepartmentCode departmentCode = DepartmentCode.of(code);

        return departmentEntityRepository.findByCode(departmentCode.getValue())
                .orElseThrow(
                        () -> new UserServiceException(
                                ErrorCode.DEPARTMENT_NOT_FOUND,
                                "Department not found with code: " + code
                        )
                );
    }

}
