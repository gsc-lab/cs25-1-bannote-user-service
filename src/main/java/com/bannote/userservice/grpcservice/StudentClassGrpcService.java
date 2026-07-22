package com.bannote.userservice.grpcservice;

import com.bannote.userservice.context.AuthorizationUtil;
import com.bannote.userservice.domain.studentclass.StudentClass;
import com.bannote.userservice.domain.studentclass.field.StudentClassCode;
import com.bannote.userservice.exception.UserServiceException;
import com.bannote.userservice.proto.student_class.v1.*;
import com.bannote.userservice.service.studentclass.StudentClassApplicationService;
import com.google.protobuf.ProtocolStringList;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.grpc.server.service.GrpcService;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
@RequiredArgsConstructor
public class StudentClassGrpcService extends StudentClassServiceGrpc.StudentClassServiceImplBase {

    private final StudentClassApplicationService service;

    @Override
    public void getStudentClass(GetStudentClassRequest request, StreamObserver<GetStudentClassResponse> responseObserver) {

        try {
            StudentClass studentClass = service.getStudentClass(request);

            GetStudentClassResponse response = GetStudentClassResponse.newBuilder()
                    .setStudentClass(studentClass.toProto())
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
    public void createStudentClass(CreateStudentClassRequest request, StreamObserver<CreateStudentClassResponse> responseObserver) {

        AuthorizationUtil.requireTA();

        try {
            StudentClass studentClass = service.createStudentClass(request);

            CreateStudentClassResponse response = CreateStudentClassResponse.newBuilder()
                    .setStudentClass(studentClass.toProto())
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
    public void updateStudentClass(UpdateStudentClassRequest request, StreamObserver<UpdateStudentClassResponse> responseObserver) {

        AuthorizationUtil.requireTA();

        try {
            StudentClass studentClass = service.updateStudentClass(request);

            UpdateStudentClassResponse response = UpdateStudentClassResponse.newBuilder()
                    .setStudentClass(studentClass.toProto())
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
    public void deleteStudentClass(DeleteStudentClassRequest request, StreamObserver<DeleteStudentClassResponse> responseObserver) {

        AuthorizationUtil.requireTA();

        try {
            StudentClass studentClass = service.deleteStudentClass(request);

            DeleteStudentClassResponse response = DeleteStudentClassResponse.newBuilder()
                    .setStudentClass(studentClass.toProto())
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
    public void listStudentClasses(ListStudentClassesRequest request, StreamObserver<ListStudentClassesResponse> responseObserver) {

        try {
            Page<StudentClass> studentClassPage = service.listStudentClasses(request);

            ListStudentClassesResponse response = ListStudentClassesResponse.newBuilder()
                    .addAllStudentClasses(studentClassPage.getContent().stream()
                            .map(StudentClass::toProto)
                            .collect(Collectors.toList()))
                    .setTotalCount(Math.toIntExact(studentClassPage.getTotalElements()))
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
    public void getManyStudentClasses(GetManyStudentClassesRequest request, StreamObserver<GetManyStudentClassesResponse> responseObserver) {

        List<StudentClass> manyStudentClasses = service.getManyStudentClasses(request);

        GetManyStudentClassesResponse response = GetManyStudentClassesResponse.newBuilder()
                .addAllStudentClasses(manyStudentClasses.stream()
                        .map(StudentClass::toProto)
                        .toList())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
