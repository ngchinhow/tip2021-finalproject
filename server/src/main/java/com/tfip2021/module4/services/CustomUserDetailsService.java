package com.tfip2021.module4.services;

import com.tfip2021.module4.models.DatabaseUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private DatabaseUserService service;

    @Override
    public DatabaseUser loadUserByUsername(final String email) {
        // Email is used as the username of the user
        DatabaseUser dbUser = service.getByEmail(email);
        if (dbUser == null) {
            throw new UsernameNotFoundException(
                "User with email " + email + " does not exist. Please sign up."
            );
        }
        return dbUser;
    }
}
