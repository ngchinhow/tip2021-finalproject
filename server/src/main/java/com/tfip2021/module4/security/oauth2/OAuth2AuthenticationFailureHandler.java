package com.tfip2021.module4.security.oauth2;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tfip2021.module4.services.TransientOAuth2AuthorizationRequestService;
import com.tfip2021.module4.utils.HttpServletRequestUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private TransientOAuth2AuthorizationRequestService service;

    @Override
    public void onAuthenticationFailure (
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException exception
    ) throws IOException {
        String state = HttpServletRequestUtils.getState(request);
        String targetUrl = service.getRedirectUri(state);
        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
            .queryParam("error", exception.getLocalizedMessage())
            .build().toUriString();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
    
}
