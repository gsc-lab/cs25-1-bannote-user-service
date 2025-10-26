package com.bannote.userservice.exception;

import io.grpc.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // User related errors
    USER_NOT_FOUND(Status.NOT_FOUND, "User not found"),
    DUPLICATE_EMAIL(Status.ALREADY_EXISTS, "Email already exists"),
    DUPLICATE_USER_CODE(Status.ALREADY_EXISTS, "User Code already exists"),

    // Department related errors
    DEPARTMENT_NOT_FOUND(Status.NOT_FOUND, "Department not found"),
    DUPLICATE_DEPARTMENT_CODE(Status.ALREADY_EXISTS, "Department Code already exists"),
    DUPLICATE_DEPARTMENT_NAME(Status.ALREADY_EXISTS, "Department Name already exists"),

    // Student class related errors
    STUDENT_CLASS_NOT_FOUND(Status.NOT_FOUND, "Student class not found"),
    STUDENT_CLASS_DEPARTMENT_MISMATCH(Status.INVALID_ARGUMENT, "Student class does not belong to the specified department"),

    // Validation errors
    REQUIRED_FIELD_MISSING(Status.INVALID_ARGUMENT, "Required field is missing"),
    INVALID_FORMAT(Status.INVALID_ARGUMENT, "Field format is invalid"),
    INVALID_ARGUMENT(Status.INVALID_ARGUMENT, "Invalid argument"),
    INVALID_STATE(Status.INTERNAL, "Invalid state"),

    ENTITY_TO_DOMAIN_CONVERSION_FAILED(Status.INTERNAL, "Failed to convert entity to domain"),
    ;

    private Status status;
    private String message;
}
