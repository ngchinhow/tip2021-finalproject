package com.tfip2021.module4.security.oauth2;

import java.util.HashMap;
import java.util.Map;

import javax.naming.AuthenticationNotSupportedException;

import com.tfip2021.module4.services.DatabaseUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class CustomOidcUserService extends OidcUserService {
    @Autowired
    private DatabaseUserService service;

    @Override
    public OidcUser loadUser(OidcUserRequest oidcUserRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(oidcUserRequest);
        String provider = oidcUserRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = new HashMap<String, Object>(oidcUser.getAttributes());
        attributes.put("idToken", oidcUser.getIdToken());
        attributes.put("userInfo", oidcUser.getUserInfo());

        try {
            return service.upsert(
                provider,
                oidcUser.getName(),
                attributes
            );
        } catch (AuthenticationNotSupportedException e) {
            throw new OAuth2AuthenticationException(null, e.getMessage(), e.getCause());
        }
    }
}
