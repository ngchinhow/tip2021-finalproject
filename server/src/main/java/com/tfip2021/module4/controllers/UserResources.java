package com.tfip2021.module4.controllers;

import com.tfip2021.module4.dto.UserResponse;
import com.tfip2021.module4.models.DatabaseUser;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class UserResources {
    @GetMapping(path = "/user/me")
    public ResponseEntity<UserResponse> getCurrentUser(
        Authentication authentication
    ) {
        DatabaseUser user = (DatabaseUser) authentication.getPrincipal();
        return ResponseEntity.ok(
            UserResponse.fromDatabaseUser(null, user)
        );
    }
}
