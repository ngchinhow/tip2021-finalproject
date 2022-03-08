package com.tfip2021.module4.repositories;

import com.tfip2021.module4.models.DatabaseEvent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DatabaseEventRepository  extends 
    JpaRepository<DatabaseEvent, Long> {

    DatabaseEvent findByProviderAndProviderEventId(String provider, String providerEventId);
}
