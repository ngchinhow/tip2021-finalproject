package com.tfip2021.module4.utils;

import com.tfip2021.module4.models.DatabaseUser;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class OAuth2Utils {
    private DatabaseUser authenticatedUser;

    public OAuth2Utils() {
        Authentication authentication = SecurityContextHolder
            .getContext().getAuthentication();
        this.authenticatedUser = (DatabaseUser) authentication.getPrincipal();
    }

    public DatabaseUser getAuthenticatedUser() {
        return this.authenticatedUser;
    }

    public String getAccessToken() {
        return this.authenticatedUser.getAccessToken();
    }

    public String getProvider() {
        return this.authenticatedUser.getProvider();
    } 
}
