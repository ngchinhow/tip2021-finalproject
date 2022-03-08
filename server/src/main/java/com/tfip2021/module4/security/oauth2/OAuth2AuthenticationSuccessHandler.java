package com.tfip2021.module4.security.oauth2;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tfip2021.module4.models.DatabaseUser;
import com.tfip2021.module4.security.jwt.JWTService;
import com.tfip2021.module4.services.model.DatabaseUserService;
import com.tfip2021.module4.services.model.TransientOAuth2AuthorizationRequestService;
import com.tfip2021.module4.utils.HttpServletRequestUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    private OAuth2AuthorizedClientService clientService;
    
    @Autowired
    private TransientOAuth2AuthorizationRequestService transientOAuth2AuthorizationRequestService;

    @Autowired
    private JWTService jwtSerivce;

    @Autowired
    private DatabaseUserService userService;

    @Override
    public void onAuthenticationSuccess (
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException {
        String targetUrl = determineTargetUrl(request, response, authentication);
        storeTokens(authentication);
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

        String jwtToken = jwtSerivce.createJWT(
            getDatabaseUser(authentication).getUserId()
        );
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

    private void storeTokens(Authentication authentication) {
        OAuth2AuthenticationToken authenticationToken = 
            (OAuth2AuthenticationToken) authentication;
        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(
            authenticationToken.getAuthorizedClientRegistrationId(),
            authenticationToken.getName()
        );
        OAuth2User oauth2User = authenticationToken.getPrincipal();
        DatabaseUser dbUser = userService.getByProviderUserId(oauth2User.getName());
        log.info(">>> access token " + client.getAccessToken().getTokenValue());
        dbUser.setAccessToken(client.getAccessToken().getTokenValue());
        dbUser.setRefreshToken(client.getRefreshToken().getTokenValue());
        userService.save(dbUser);
    }

    private DatabaseUser getDatabaseUser(Authentication authentication) {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        return userService.getByProviderUserId(oauth2User.getName());
    }
}
