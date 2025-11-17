package com.bannote.userservice.grpcservice;

import com.bannote.userservice.context.AuthorizationUtil;
import com.bannote.userservice.domain.allowedDomain.AllowedDomain;
import com.bannote.userservice.proto.allowed_domain.v1.*;
import com.bannote.userservice.service.alloweddomain.AllowedDomainApplicationService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class AllowedDomainGrpcService extends AllowedDomainServiceGrpc.AllowedDomainServiceImplBase {

    private final AllowedDomainApplicationService service;

    @Override
    public void addAllowedDomain(AddAllowedDomainRequest request, StreamObserver<AddAllowedDomainResponse> responseObserver) {

        AuthorizationUtil.requireTA();

        AllowedDomain allowedDomain = service.createAllowedDomain(request);

        AddAllowedDomainResponse response = AddAllowedDomainResponse.newBuilder()
                .setAllowedDomain(allowedDomain.toProto())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void removeAllowedDomain(RemoveAllowedDomainRequest request, StreamObserver<RemoveAllowedDomainResponse> responseObserver) {

        AuthorizationUtil.requireTA();

        AllowedDomain allowedDomain = service.deleteAllowedDomain(request);

        RemoveAllowedDomainResponse response = RemoveAllowedDomainResponse.newBuilder()
                .setAllowedDomain(allowedDomain.toProto())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void listAllowedDomain(ListAllowedDomainRequest request, StreamObserver<ListAllowedDomainResponse> responseObserver) {

        AuthorizationUtil.requireClassRep();

        Page<AllowedDomain> allowedDomainsPage = service.listAllowedDomains(request);

        ListAllowedDomainResponse response = ListAllowedDomainResponse.newBuilder()
                .addAllAllowedDomain(allowedDomainsPage.getContent().stream()
                        .map(AllowedDomain::toProto)
                        .toList())
                .setTotalCount(Math.toIntExact(allowedDomainsPage.getTotalElements()))
                .setPage(request.getPage())
                .setSize(request.getSize())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
