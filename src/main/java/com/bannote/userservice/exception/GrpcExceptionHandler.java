package com.bannote.userservice.exception;

import io.grpc.*;
import lombok.extern.slf4j.Slf4j;

/**
 * gRPC 전역 예외 핸들러
 */
@Slf4j
public class GrpcExceptionHandler implements ServerInterceptor {

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        return new ExceptionHandlingListener<>(next.startCall(call, headers), call);
    }

    private static class ExceptionHandlingListener<ReqT, RespT>
            extends ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT> {

        private final ServerCall<ReqT, RespT> call;

        ExceptionHandlingListener(ServerCall.Listener<ReqT> delegate, ServerCall<ReqT, RespT> call) {
            super(delegate);
            this.call = call;
        }

        @Override
        public void onHalfClose() {
            try {
                super.onHalfClose();
            } catch (Exception e) {
                handleException(e);
            }
        }

        @Override
        public void onMessage(ReqT message) {
            try {
                super.onMessage(message);
            } catch (Exception e) {
                handleException(e);
            }
        }

        private void handleException(Exception e) {
            if (e instanceof UserServiceException userServiceException) {
                log.error("UserServiceException - {}: {}",
                    userServiceException.getErrorCode(), userServiceException.getMessage());

                call.close(
                    userServiceException.getErrorCode().getStatus()
                        .withDescription(userServiceException.getMessage()),
                    new Metadata()
                );
            } else if (e instanceof StatusRuntimeException statusRuntimeException) {
                throw statusRuntimeException;
            } else {
                log.error("Unexpected exception", e);
                call.close(
                    Status.INTERNAL.withDescription("Internal server error: " + e.getMessage()),
                    new Metadata()
                );
            }
            if (e instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }
            throw new RuntimeException(e);
        }
    }
}