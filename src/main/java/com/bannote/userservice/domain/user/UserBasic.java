package com.bannote.userservice.domain.user;

import com.bannote.userservice.domain.user.field.*;
import com.bannote.userservice.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Builder
@Getter
public class UserBasic {

    private final Long userId;
    private final UserCode userCode;
    private final UserEmail userEmail;
    private final String userName;
    private final UserFamilyName userFamilyName;
    private final UserGivenName userGivenName;
    private final UserType userType;
    private final UserStatus userStatus;
    private final UserBio userBio;
    private final UserProfileImage userProfileImage;
    private final Timestamp createdAt;
    private final Timestamp deletedAt;

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
        return UserBasic.builder()
                .userCode(userCode)
                .userEmail(userEmail)
                .userFamilyName(userFamilyName)
                .userGivenName(userGivenName)
                .userType(userType)
                .userStatus(userStatus)
                .userProfileImage(userProfileImage)
                .build();
    }

    public static UserBasic fromEntity(UserEntity userEntity) {
        return UserBasic.builder()
                .userId(userEntity.getId())
                .userCode(UserCode.of(userEntity.getCode()))
                .userEmail(UserEmail.of(userEntity.getEmail()))
                .userName(userEntity.getName())
                .userFamilyName(UserFamilyName.of(userEntity.getFamilyName()))
                .userGivenName(UserGivenName.of(userEntity.getGivenName()))
                .userType(userEntity.getType())
                .userStatus(userEntity.getStatus())
                .userBio(UserBio.of(userEntity.getBio()))
                .userProfileImage(UserProfileImage.of(userEntity.getProfileImage()))
                .createdAt(userEntity.getCreatedAt())
                .deletedAt(userEntity.getDeletedAt())
                .build();
    }

    public com.bannote.userservice.proto.user.v1.UserBasic toProto() {
        return com.bannote.userservice.proto.user.v1.UserBasic.newBuilder()
                .setUserCode(this.userCode.getValue())
                .setUserEmail(this.userEmail.getValue())
                .setUserName(this.userName)
                .setFamilyName(this.userFamilyName.getValue())
                .setGivenName(this.userGivenName.getValue())
                .setType(this.userType.toProto())
                .setStatus(this.userStatus.toProto())
                .setBio(this.userBio.getValue())
                .setProfileImageUrl(this.userProfileImage.getValue())
                .setCreatedAt(this.createdAt != null ? com.google.protobuf.util.Timestamps.fromMillis(this.createdAt.getTime()) : null)
                .setDeletedAt(this.deletedAt != null ? com.google.protobuf.util.Timestamps.fromMillis(this.deletedAt.getTime()) : null)
                .build();
    }

}
