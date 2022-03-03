package com.tfip2021.module4.security.oauth2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tfip2021.module4.services.TransientOAuth2AuthorizationRequestService;
import com.tfip2021.module4.utils.HttpServletRequestUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class SQLOAuth2AuthorizationRequestRepository implements
    AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    @Autowired
    private TransientOAuth2AuthorizationRequestService service;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        // Copied from org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository
        Assert.notNull(request, "request cannot be null");
        String state = HttpServletRequestUtils.getState(request);
        if (state == null) {
            return null;
        }
        // Pull OAuth2AuthorizationRequest from MySQL
        return service.getOAuth2AuthorizationRequest(state);
    }

    @Override
    public void saveAuthorizationRequest(
        OAuth2AuthorizationRequest authorizationRequest,
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        // Copied from org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(response, "response cannot be null");
        if (authorizationRequest == null) {
            this.removeAuthorizationRequest(request, response);
            return;
        }
        String state = authorizationRequest.getState();
        Assert.hasText(state, "authorizationRequest.state cannot be empty");
        // Store OAuth2AuthorizationRequest into MySQL
        String redirectUriAfterLogin = HttpServletRequestUtils.getRedirectUri(request);
        service.saveAuthorizationRequest(state, authorizationRequest, redirectUriAfterLogin);
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        // Copied from org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(response, "response cannot be null");

        /* 
        * Read only OAuth2AuthorizationRequest from MySQL.
        * OAuth2LoginAuthenticationFilter.attemptAuthorization calls this method
        * at the beginning of the authorization flow which would have removed
        * the entire entry from MySQL. Undesired behaviour, as Redirect URI will
        * be removed as well. It is required for redirect after successful
        * authentication. Entry will be deleted in 
        * OAuth2AuthenticationSuccessHandler.onAuthenticationSuccess instead.
        */
        return this.loadAuthorizationRequest(request);
    }

    @Override
    @Deprecated
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
        // Deprecated method; forced to implement bacause of interface.
        return null;
    }
}