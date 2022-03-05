package com.tfip2021.module4.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.naming.AuthenticationNotSupportedException;

import com.tfip2021.module4.dto.LoginRequest;
import com.tfip2021.module4.dto.SignUpRequest;
import com.tfip2021.module4.dto.UserResponse;
import com.tfip2021.module4.models.DatabaseUser;
import com.tfip2021.module4.security.exceptions.DuplicateUserException;
import com.tfip2021.module4.security.jwt.JWTService;
import com.tfip2021.module4.services.DatabaseUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/api/auth/basic")
public class BasicAuthResources {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DatabaseUserService databaseUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtSerivce;

    @PostMapping(path = "/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword()
            ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        DatabaseUser dbUser = (DatabaseUser) authentication.getPrincipal();
        String jwt = jwtSerivce.createJWT(dbUser.getUserId());
        return new ResponseEntity<> (
            UserResponse.fromDatabaseUser(jwt, dbUser),
            HttpStatus.OK
        );
    }

    @PostMapping(path = "/signup")
    public ResponseEntity<String> signup(
        @RequestBody SignUpRequest signupRequest
    ) throws AuthenticationNotSupportedException {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("displayName", signupRequest.getDisplayName());
        attributes.put("email", signupRequest.getEmail());
        log.info(passwordEncoder.encode(signupRequest.getPassword()));
        attributes.put(
            "password",
            passwordEncoder.encode(signupRequest.getPassword())
        );
        try {
            databaseUserService.upsert(
                "local",
                signupRequest.getEmail(), // Email used as Provider User Id
                attributes
            );
        } catch (DuplicateUserException e) {
            log.warn(e.getMessage());
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                e.getLocalizedMessage(),
                e.getCause()
            );
        }
        return ResponseEntity.accepted().build();
    }
}
