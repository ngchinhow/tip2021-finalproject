package com.tfip2021.module4.services;

import java.util.Map;
import java.util.Optional;

import javax.naming.AuthenticationNotSupportedException;

import com.tfip2021.module4.models.DatabaseUser;
import com.tfip2021.module4.repositories.DatabaseUserRepository;
import com.tfip2021.module4.security.exceptions.DuplicateUserException;
import com.tfip2021.module4.services.disseminators.PropertiesDisseminatorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
@Service
public class DatabaseUserService {
    @Autowired
    private DatabaseUserRepository repo;

    public Optional<DatabaseUser> getById(Long userId) {
        return repo.findById(userId);
    }

    public DatabaseUser getByEmail(String email) {
        return repo.findByEmail(email);
    }

    public DatabaseUser upsert(
        String provider,
        String providerUserId,
        Map<String, Object> attributes
    ) throws AuthenticationNotSupportedException {
        String userEmail = PropertiesDisseminatorFactory
            .getUserEmail(provider, attributes);
        DatabaseUser dbUser = this.getByEmail(userEmail);
        if (dbUser != null) {
            if (!dbUser.getProvider().equals(provider)) {
                String existingUserProvider = dbUser.getProvider();
                throw new DuplicateUserException(
                    "You have already signed up with " + 
                    StringUtils.capitalize(existingUserProvider) +
                    ". Please log in via that method instead."
                );
            } else {
                dbUser = update(dbUser, provider, providerUserId, attributes);
            }
        } else {
            dbUser = create(provider, providerUserId, attributes);
        }
        dbUser = repo.save(dbUser);
        repo.flush();
        // Properties in DB and Object are different, so properties need to
        // be repopulated after inserting into DB
        return update(dbUser, provider, providerUserId, attributes);
    }

    public DatabaseUser create(
        String provider,
        String providerUserId,
        Map<String, Object> attributes
    ) throws AuthenticationNotSupportedException {
        return PropertiesDisseminatorFactory
            .mergeUserWithAttributes(
                new DatabaseUser(),
                "create",
                provider,
                providerUserId, attributes
            );
    }

    public DatabaseUser update(
        DatabaseUser dbUser,
        String provider,
        String providerUserId,
        Map<String, Object> attributes
    ) throws AuthenticationNotSupportedException {
        return PropertiesDisseminatorFactory
            .mergeUserWithAttributes(
                dbUser,
                "update",
                provider,
                providerUserId,
                attributes
            );
    }
} 