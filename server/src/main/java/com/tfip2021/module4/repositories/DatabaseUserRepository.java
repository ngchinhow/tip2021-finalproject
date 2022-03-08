package com.tfip2021.module4.repositories;

import com.tfip2021.module4.models.DatabaseUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatabaseUserRepository extends
    JpaRepository<DatabaseUser, Long> {

    DatabaseUser findByProviderUserId(String providerUserId);
        
    DatabaseUser findByEmail(String email);
}
