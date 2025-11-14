package com.bannote.userservice.grpcservice;

import com.bannote.userservice.context.AuthorizationUtil;
import com.bannote.userservice.domain.user.UserDetail;
import com.bannote.userservice.exception.UserServiceException;
import com.bannote.userservice.proto.user.v1.*;
import com.bannote.userservice.service.user.UserApplicationService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.grpc.server.service.GrpcService;

import java.util.stream.Collectors;

@GrpcService
@RequiredArgsConstructor
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {

    private final UserApplicationService userApplicationService;

    @Override
    public void userLogin(UserLoginRequest request, StreamObserver<UserLoginResponse> responseObserver) {

        UserLoginResponse userLoginResponse = userApplicationService.userLogin(request.getEmail());
        responseObserver.onNext(userLoginResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void createUser(CreateUserRequest request, StreamObserver<CreateUserResponse> responseObserver) {

        try {
            UserDetail userDetail = switch (request.getUserType()) {
                case USER_TYPE_STUDENT -> userApplicationService.createStudent(request);
                case USER_TYPE_EMPLOYEE -> userApplicationService.createEmployee(request);
                default -> userApplicationService.createUser(request);
            };

            CreateUserResponse createUserResponse = CreateUserResponse.newBuilder()
                    .setSuccess(true)
                    .setCanLogin(userDetail.isLoginAllowed())
                    .setUser(userDetail.toProto())
                    .build();

            responseObserver.onNext(createUserResponse);
            responseObserver.onCompleted();

        } catch (UserServiceException e) {
            CreateUserResponse createUserResponse = CreateUserResponse.newBuilder()
                    .setSuccess(false)
                    .setCanLogin(false)
                    .setReason(e.getMessage())
                    .build();

            responseObserver.onNext(createUserResponse);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void listUsers(ListUsersRequest request, StreamObserver<ListUsersResponse> responseObserver) {

        AuthorizationUtil.requireClassRep();

        Page<UserDetail> userDetails = userApplicationService.listUsers(request);

        ListUsersResponse response = ListUsersResponse.newBuilder()
                .addAllUsers(userDetails.getContent().stream()
                        .map(UserDetail::toProto)
                        .collect(Collectors.toList()))
                .setTotalCount(Math.toIntExact(userDetails.getTotalElements()))
                .setPage(request.getPage())
                .setSize(request.getSize())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
