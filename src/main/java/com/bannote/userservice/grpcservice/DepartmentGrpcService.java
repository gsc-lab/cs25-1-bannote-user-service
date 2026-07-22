package com.bannote.userservice.grpcservice;

import com.bannote.userservice.context.AuthorizationUtil;
import com.bannote.userservice.domain.department.Department;
import com.bannote.userservice.exception.UserServiceException;
import com.bannote.userservice.proto.department.v1.*;
import com.bannote.userservice.service.department.DepartmentApplicationService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.grpc.server.service.GrpcService;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
@RequiredArgsConstructor
public class DepartmentGrpcService extends DepartmentServiceGrpc.DepartmentServiceImplBase {

    private final DepartmentApplicationService departmentApplicationService;

    @Override
    public void createDepartment(CreateDepartmentRequest request, StreamObserver<CreateDepartmentResponse> responseObserver) {

        AuthorizationUtil.requireTA();

        try {
            Department department = departmentApplicationService.createDepartment(request);

            CreateDepartmentResponse response = CreateDepartmentResponse.newBuilder()
                    .setDepartment(department.toProto())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (UserServiceException e) {
            responseObserver.onError(
                    e.getErrorCode().getStatus()
                            .withDescription(e.getMessage())
                            .asRuntimeException()
            );
        }

    }

    @Override
    public void getDepartment(GetDepartmentRequest request, StreamObserver<GetDepartmentResponse> responseObserver) {

        try {
            Department department = departmentApplicationService.getDepartment(request);

            GetDepartmentResponse response = GetDepartmentResponse.newBuilder()
                    .setDepartment(department.toProto())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (UserServiceException e) {
            responseObserver.onError(
                    e.getErrorCode().getStatus()
                            .withDescription(e.getMessage())
                            .asRuntimeException()
            );
        }
    }

    @Override
    public void updateDepartment(UpdateDepartmentRequest request, StreamObserver<UpdateDepartmentResponse> responseObserver) {

        AuthorizationUtil.requireTA();

        try {
            Department department = departmentApplicationService.updateDepartment(request);

            UpdateDepartmentResponse response = UpdateDepartmentResponse.newBuilder()
                    .setDepartment(department.toProto())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (UserServiceException e) {
            responseObserver.onError(
                    e.getErrorCode().getStatus()
                            .withDescription(e.getMessage())
                            .asRuntimeException()
            );
        }
    }

    @Override
    public void deleteDepartment(DeleteDepartmentRequest request, StreamObserver<DeleteDepartmentResponse> responseObserver) {

        AuthorizationUtil.requireProfessor();

        try {
            Department department = departmentApplicationService.deleteDepartment(request);

            DeleteDepartmentResponse response = DeleteDepartmentResponse.newBuilder()
                    .setDepartment(department.toProto())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (UserServiceException e) {
            responseObserver.onError(
                    e.getErrorCode().getStatus()
                            .withDescription(e.getMessage())
                            .asRuntimeException()
            );
        }
    }

    @Override
    public void listDepartments(ListDepartmentsRequest request, StreamObserver<ListDepartmentsResponse> responseObserver) {

        try {
            Page<Department> departmentPage = departmentApplicationService.listDepartments(request);

            ListDepartmentsResponse response = ListDepartmentsResponse.newBuilder()
                    .addAllDepartments(departmentPage.getContent().stream()
                            .map(Department::toProto)
                            .collect(Collectors.toList()))
                    .setTotalCount(Math.toIntExact(departmentPage.getTotalElements()))
                    .setPage(request.getPage())
                    .setSize(request.getSize())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (UserServiceException e) {
            responseObserver.onError(
                    e.getErrorCode().getStatus()
                            .withDescription(e.getMessage())
                            .asRuntimeException()
            );
        }
    }

    @Override
    public void getManyDepartments(GetManyDepartmentsRequest request, StreamObserver<GetManyDepartmentsResponse> responseObserver) {

        List<Department> manyDepartments = departmentApplicationService.getManyDepartments(request);

        GetManyDepartmentsResponse response = GetManyDepartmentsResponse.newBuilder()
                .addAllDepartments(manyDepartments.stream()
                        .map(Department::toProto)
                        .toList()
                ).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
