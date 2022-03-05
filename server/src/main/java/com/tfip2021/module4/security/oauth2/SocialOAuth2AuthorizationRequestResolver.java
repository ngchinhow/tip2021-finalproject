/*
* Copied pieces from org.springframework.security.oauth2.client.web.
* DefaultOAuth2AuthorizationRequestResolver because I need to retrieve the 
* Client Registration ID from the request, but its methods are (almost) all
* private and the class itself is final.
*/
package com.tfip2021.module4.security.oauth2;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.tfip2021.module4.dto.SocialProvider;
import static com.tfip2021.module4.models.Constants.OAUTH2_AUTHORIZATION_REQUEST_REGISTRATION_ID_ATTRIBUTE_NAME;

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;


public class SocialOAuth2AuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

    private OAuth2AuthorizationRequestResolver defaultResolver;

    public SocialOAuth2AuthorizationRequestResolver(
        ClientRegistrationRepository repo,
        String authorizationRequestBaseUri
    ) {
        this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(
            repo, authorizationRequestBaseUri
        );
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        OAuth2AuthorizationRequest oauth2AuthorizationRequest = defaultResolver
            .resolve(request);
        if (oauth2AuthorizationRequest != null) {
            oauth2AuthorizationRequest = customizeAuthorizationRequest(
                oauth2AuthorizationRequest
            );
        }
        return oauth2AuthorizationRequest;
    }

    @Override
    public OAuth2AuthorizationRequest resolve(
        HttpServletRequest request,
        String clientRegistrationId
    ) {
        OAuth2AuthorizationRequest oauth2AuthorizationRequest = defaultResolver
            .resolve(request, clientRegistrationId);
        if (oauth2AuthorizationRequest != null) {
            oauth2AuthorizationRequest = customizeAuthorizationRequest(
                oauth2AuthorizationRequest
            );
        }
        return oauth2AuthorizationRequest;
    }
    
    private OAuth2AuthorizationRequest customizeAuthorizationRequest(
        OAuth2AuthorizationRequest oauth2AuthorizationRequest
    ) {
        OAuth2AuthorizationRequest req = oauth2AuthorizationRequest;
        String registrationId = req.getAttribute(
            OAUTH2_AUTHORIZATION_REQUEST_REGISTRATION_ID_ATTRIBUTE_NAME
        );
        // Modify Authorization Request based on Social Provider
        if (registrationId.equals(SocialProvider.GOOGLE.getLoginProvider())) {
            Map<String, Object> additionalParams = new HashMap<>();
            additionalParams.putAll(req.getAdditionalParameters());
            additionalParams.put("access_type", "offline");
            return OAuth2AuthorizationRequest.from(req)
                .additionalParameters(additionalParams)
                .build();
        }
        // Add more customizations to Social Providers here
        else {
            return req;
        }
    }
}
