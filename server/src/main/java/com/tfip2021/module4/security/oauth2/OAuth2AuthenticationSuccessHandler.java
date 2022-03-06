package com.tfip2021.module4.security.oauth2;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tfip2021.module4.models.DatabaseUser;
import com.tfip2021.module4.security.jwt.JWTService;
import com.tfip2021.module4.services.TransientOAuth2AuthorizationRequestService;
import com.tfip2021.module4.utils.HttpServletRequestUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    
    @Autowired
    private TransientOAuth2AuthorizationRequestService transientOAuth2AuthorizationRequestService;

    @Autowired
    private JWTService jwtSerivce;

    @Override
    public void onAuthenticationSuccess (
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException {
        String targetUrl = determineTargetUrl(request, response, authentication);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    @Override
    public String determineTargetUrl(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) {
        String state = HttpServletRequestUtils.getState(request);
        String targetUrl = 
            transientOAuth2AuthorizationRequestService.getRedirectUri(state);
        transientOAuth2AuthorizationRequestService.deleteByState(state);
        DatabaseUser dbUser = (DatabaseUser) authentication.getPrincipal();
        String jwtToken = jwtSerivce.createJWT(dbUser.getUserId());
        URI uri = URI.create(targetUrl);
        String fragment = uri.getFragment().substring(1);
        try {
            return UriComponentsBuilder.fromUri(
                new URI(uri.getScheme(), uri.getSchemeSpecificPart(), null)
            )
            .pathSegment("#", fragment)
            .queryParam("#token", jwtToken)
            .build()
            .toString();
        } catch (IllegalArgumentException | URISyntaxException e) {
            log.error(e.getMessage());
        }
        return targetUrl;
    }
}
