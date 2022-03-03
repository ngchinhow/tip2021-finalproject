package com.tfip2021.module4.repositories;

import java.util.List;

import com.tfip2021.module4.models.TransientOAuth2AuthorizationRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransientOAuth2AuthorizationRequestRepository extends
    JpaRepository<TransientOAuth2AuthorizationRequest, String> {

    TransientOAuth2AuthorizationRequest findByState(String state);

    List<TransientOAuth2AuthorizationRequest> deleteByState(String state);
}
