package com.tfip2021.module4.services.model;

import java.util.List;

import com.tfip2021.module4.models.TransientOAuth2AuthorizationRequest;
import com.tfip2021.module4.repositories.TransientOAuth2AuthorizationRequestRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TransientOAuth2AuthorizationRequestService {
    @Autowired
    private TransientOAuth2AuthorizationRequestRepository repo;

    public OAuth2AuthorizationRequest getOAuth2AuthorizationRequest(String state) {
        return repo.findByState(state).getOauth2AuthorizationRequest();
    }

    public String getRedirectUri(String state) {
        return repo.findByState(state).getRedirectUri();
    }

    public void saveAuthorizationRequest(
        String state,
        OAuth2AuthorizationRequest authorizationRequest,
        String redirectUri
    ) {
        TransientOAuth2AuthorizationRequest transientOAuth2AuthorizationRequest = 
            new TransientOAuth2AuthorizationRequest();
        transientOAuth2AuthorizationRequest.setState(state);
        transientOAuth2AuthorizationRequest
            .setOauth2AuthorizationRequest(authorizationRequest);
        transientOAuth2AuthorizationRequest.setRedirectUri(redirectUri);
        repo.save(transientOAuth2AuthorizationRequest);
    }

    public OAuth2AuthorizationRequest deleteByState(String state) {
        List<TransientOAuth2AuthorizationRequest> deleted = 
            repo.deleteByState(state);
        if (deleted.isEmpty()) {
            throw new IllegalArgumentException(
                "TransientOAuth2AuthorizationRequest with state " + state +
                "does not exist"
            );
        }
        
        return deleted.get(0).getOauth2AuthorizationRequest();
    }
}
