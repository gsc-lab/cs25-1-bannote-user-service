package com.bannote.userservice.domain.user;

import com.bannote.userservice.domain.user.field.*;
import com.bannote.userservice.entity.UserEntity;
import lombok.Getter;
import lombok.With;

import java.sql.Timestamp;

@Getter
public class UserBasic {

    private final Long userId;
    private final UserCode userCode;
    private final UserEmail userEmail;
    @With private final UserFamilyName userFamilyName;
    @With private final UserGivenName userGivenName;
    @With private final UserType userType;
    @With private final UserStatus userStatus;
    @With private final UserBio userBio;
    @With private final UserProfileImage userProfileImage;
    private final Timestamp createdAt;
    private final Timestamp deletedAt;

    private UserBasic(
            Long userId,
            UserCode userCode,
            UserEmail userEmail,
            UserFamilyName userFamilyName,
            UserGivenName userGivenName,
            UserType userType,
            UserStatus userStatus,
            UserBio userBio,
            UserProfileImage userProfileImage,
            Timestamp createdAt,
            Timestamp deletedAt
    ) {
        this.userId = userId;
        this.userCode = userCode;
        this.userEmail = userEmail;
        this.userFamilyName = userFamilyName;
        this.userGivenName = userGivenName;
        this.userType = userType;
        this.userStatus = userStatus;
        this.userBio = userBio != null ? userBio : UserBio.of("");
        this.userProfileImage = userProfileImage != null ? userProfileImage : UserProfileImage.of("");
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
    }

    public String getUserName() {
         return String.format(this.userFamilyName.getValue() + " " + this.userGivenName.getValue());
    }

    /**
     * 회원 가입시 사용
     * @param userStatus 외부에서 이메일 허용 여부 조회 후 주입
     */
    public static UserBasic create(
            UserCode userCode,
            UserEmail userEmail,
            UserFamilyName userFamilyName,
            UserGivenName userGivenName,
            UserType userType,
            UserStatus userStatus,
            UserProfileImage userProfileImage
            ) {
        return new UserBasic(
                null,
                userCode,
                userEmail,
                userFamilyName,
                userGivenName,
                userType,
                userStatus,
                UserBio.of(""),
                userProfileImage != null ? userProfileImage : UserProfileImage.of(""),
                null,
                null
        );
    }

    public static UserBasic fromEntity(UserEntity userEntity) {
        return new UserBasic(
                userEntity.getId(),
                UserCode.of(userEntity.getCode()),
                UserEmail.of(userEntity.getEmail()),
                UserFamilyName.of(userEntity.getFamilyName()),
                UserGivenName.of(userEntity.getGivenName()),
                userEntity.getType(),
                userEntity.getStatus(),
                UserBio.of(userEntity.getBio()),
                UserProfileImage.of(userEntity.getProfileImage()),
                userEntity.getCreatedAt(),
                userEntity.getDeletedAt()
        );
    }

    public com.bannote.userservice.proto.user.v1.UserBasic toProto() {
        var builder = com.bannote.userservice.proto.user.v1.UserBasic.newBuilder()
                .setUserCode(this.userCode.getValue())
                .setUserEmail(this.userEmail.getValue())
                .setUserName(this.getUserName())
                .setFamilyName(this.userFamilyName.getValue())
                .setGivenName(this.userGivenName.getValue())
                .setType(this.userType.toProto())
                .setStatus(this.userStatus.toProto())
                .setBio(this.userBio.getValue())
                .setProfileImageUrl(this.userProfileImage.getValue())
                .setCreatedAt(this.createdAt != null ? com.google.protobuf.util.Timestamps.fromMillis(this.createdAt.getTime()) : null);

        if (this.deletedAt != null) {
            builder.setDeletedAt(com.google.protobuf.util.Timestamps.fromMillis(this.deletedAt.getTime()));
        }

        return builder.build();
    }
}
