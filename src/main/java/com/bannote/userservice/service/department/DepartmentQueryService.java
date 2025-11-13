package com.bannote.userservice.service.department;

import com.bannote.userservice.domain.department.Department;
import com.bannote.userservice.domain.department.field.DepartmentCode;
import com.bannote.userservice.domain.department.field.DepartmentName;
import com.bannote.userservice.entity.DepartmentEntity;
import com.bannote.userservice.exception.ErrorCode;
import com.bannote.userservice.exception.UserServiceException;
import com.bannote.userservice.repository.DepartmentEntityRepository;
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
public class DepartmentQueryService {

    private final DepartmentEntityRepository departmentEntityRepository;

    /**
     * 학과 코드로 학과 엔티티 조회
     * @param code - 학과 코드 (String)
     * @return  DepartmentEntity 객체
     * @throws UserServiceException 학과를 찾을 수 없을 때 발생
     */
    public DepartmentEntity getDepartmentEntityByCode(String code) throws UserServiceException {

        DepartmentCode.validate(code);

        return departmentEntityRepository.findByCode(code)
                .orElseThrow(() -> new UserServiceException(
                        ErrorCode.DEPARTMENT_NOT_FOUND,
                        "Department not found with code: " + code
                        )
                );
    }

    /**
     * 학과 코드로 학과 도메인 조회
     * @param code - 학과 코드 (String)
     * @return 조회된 Department 객체
     */
    public Department getDepartmentByCode(String code) {

        DepartmentCode.validate(code);

        return Department.fromEntity(departmentEntityRepository.findByCode(code)
                .orElseThrow(() -> new UserServiceException(
                        ErrorCode.DEPARTMENT_NOT_FOUND,
                        "Department not found with code: " + code
                    )
                )
        );
    }

    /**
     * 학과 코드 존재 여부 확인
     * @param code DepartmentCode 객체
     * @return  존재하면 true, 없으면 false
     */
    public Boolean existsByCode(DepartmentCode code) {
        return departmentEntityRepository.existsByCode(code.getValue());
    }

    /**
     * 학과 이름 존재 여부 확인
     * @param name DepartmentName 객체
     * @return  존재하면 true, 없으면 false
     */
    public Boolean existsByName(DepartmentName name) {
        return departmentEntityRepository.existsByName(name.getValue());
    }

    /**
     * 학과 목록 페이징 조회
     * @param page 페이지 번호 (0부터 시작)
     * @param size 페이지 크기
     * @return 페이징된 학과 엔티티 목록
     */
    public Page<DepartmentEntity> listDepartments(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return departmentEntityRepository.findAll(pageable);
    }

    public List<DepartmentEntity> getManyDepartments(List<DepartmentCode> departmentCodesList) {

        List<String> codes = departmentCodesList.stream()
                .map(DepartmentCode::getValue)
                .toList();

        return departmentEntityRepository.findAllByCodeIn(codes);
    }
}
