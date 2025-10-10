package com.bannote.userservice.domain.user;

import com.bannote.userservice.exception.ErrorCode;
import com.bannote.userservice.exception.UserServiceException;
import lombok.Getter;

@Getter
public class UserDetail {

    private final UserBasic userBasic;
    private final Student student;
    private final Employee employee;

    private UserDetail(UserBasic userBasic, Student student, Employee employee) {
        int count = (userBasic != null ? 1 : 0)
                + (student != null ? 1 : 0)
                + (employee != null ? 1 : 0);

        if (count != 1) {
            throw new UserServiceException(ErrorCode.INVALID_ARGUMENT,
                String.format("Exactly one user detail must be provided: UserBasic, Student, or Employee: %d provided", count));
        }

        this.userBasic = userBasic;
        this.student = student;
        this.employee = employee;
    }

    public static UserDetail ofBasic(UserBasic userBasic) {
        return new UserDetail(userBasic, null, null);
    }

    public static UserDetail ofStudent(Student student) {
        return new UserDetail(null, student, null);
    }

    public static UserDetail ofEmployee(Employee employee) {
        return new UserDetail(null, null, employee);
    }

    public boolean isStudent() {
        return student != null;
    }

    public boolean isEmployee() {
        return employee != null;
    }

    public boolean isBasic() {
        return userBasic != null;
    }

    public com.bannote.userservice.proto.user.v1.UserDetail toProto() {
        var builder = com.bannote.userservice.proto.user.v1.UserDetail.newBuilder();

        if (this.userBasic != null) {
            builder.setUser(this.userBasic.toProto());
        } else if (this.student != null) {
            builder.setStudent(this.student.toProto());
        } else if (this.employee != null) {
            builder.setEmployee(this.employee.toProto());
        }

        return builder.build();
    }
}