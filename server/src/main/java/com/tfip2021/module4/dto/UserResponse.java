package com.tfip2021.module4.dto;

import com.tfip2021.module4.models.DatabaseUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class UserResponse {
    private String jwt;
    private long userId;
    private String displayName;
    private String email;
    private String provider;
    private String providerUserId;
    private String profilePictureUrl;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    public static UserResponse fromDatabaseUser(String jwt, DatabaseUser dbUser) {
        return UserResponse.builder()
            .jwt(jwt)
            .userId(dbUser.getUserId())
            .displayName(dbUser.getDisplayName())
            .email(dbUser.getEmail())
            .provider(dbUser.getProvider())
            .providerUserId(dbUser.getProviderUserId())
            .profilePictureUrl(dbUser.getProfilePictureUrl())
            .accountNonExpired(dbUser.isAccountNonExpired())
            .accountNonLocked(dbUser.isAccountNonLocked())
            .credentialsNonExpired(dbUser.isCredentialsNonExpired())
            .enabled(dbUser.isEnabled())
            .build();
    }
}
