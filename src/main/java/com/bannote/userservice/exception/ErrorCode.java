package com.bannote.userservice.exception;

import io.grpc.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND(Status.NOT_FOUND, "User not founded"),
    REQUIRED_FIELD_MISSING(Status.INVALID_ARGUMENT, "Required field is missing"),
    INVALID_FORMAT(Status.INVALID_ARGUMENT, "Field format is invalid"),
    ;

    private Status status;
    private String message;
}
