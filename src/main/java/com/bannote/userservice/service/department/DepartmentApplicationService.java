package com.bannote.userservice.service.department;

import com.bannote.userservice.domain.department.Department;
import com.bannote.userservice.domain.department.field.DepartmentCode;
import com.bannote.userservice.domain.department.field.DepartmentName;
import com.bannote.userservice.entity.DepartmentEntity;
import com.bannote.userservice.proto.department.v1.*;
import com.google.protobuf.ProtocolStringList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DepartmentApplicationService {

    private final DepartmentCommandService departmentCommandService;
    private final DepartmentQueryService departmentQueryService;

    /**
     * 학과 생성
     * @param request CreateDepartmentRequest 객체
     * @return 생성된 Department 객체
     */
    public Department createDepartment(CreateDepartmentRequest request) {

        Department department = Department.create(
                DepartmentCode.of(request.getDepartmentCode()),
                DepartmentName.of(request.getDepartmentName())
        );

        return departmentCommandService.createDepartment(department);
    }

    /**
     * 학과 조회
     * @param request GetDepartmentRequest 객체 (학과 코드)
     * @return 조회된 Department 객체
     */
    public Department getDepartment(GetDepartmentRequest request) {

        return departmentQueryService.getDepartmentByCode(request.getDepartmentCode());
    }

    /**
     * 학과 수정
     * @param request UpdateDepartmentRequest 객체
     * @return 수정된 Department 객체
     */
    public Department updateDepartment(UpdateDepartmentRequest request) {

        Department department = Department.update(
                DepartmentCode.of(request.getDepartmentCode()),
                request.hasName() ? DepartmentName.of(request.getName()) : null
        );

        return departmentCommandService.updateDepartment(department);
    }

    /**
     * 학과 삭제
     * @param request DeleteDepartmentRequest 객체
     * @return 삭제된 Department 객체
     */
    public Department deleteDepartment(DeleteDepartmentRequest request) {

        return departmentCommandService.deleteDepartment(DepartmentCode.of(request.getDepartmentCode()));
    }

    /**
     * 학과 목록 페이징 조회
     * @param request ListDepartmentsRequest 객체 (page, size)
     * @return 페이징된 Department 객체
     */
    public Page<Department> listDepartments(ListDepartmentsRequest request) {

        Page<DepartmentEntity> departmentEntities = departmentQueryService.listDepartments(
                request.getPage(),
                request.getSize()
        );

        return departmentEntities.map(Department::fromEntity);
    }

    public List<Department> getManyDepartments(GetManyDepartmentsRequest request) {

        ProtocolStringList departmentCodesStringList = request.getDepartmentCodesList();

        List<DepartmentCode> departmentCodesList = departmentCodesStringList.stream()
                .map(DepartmentCode::of)
                .toList();

        return departmentQueryService.getManyDepartments(departmentCodesList).stream()
                .map(Department::fromEntity)
                .toList();
    }
}
