package com.tfip2021.module4.security.oauth2;

import javax.naming.AuthenticationNotSupportedException;

import com.tfip2021.module4.services.DatabaseUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private DatabaseUserService service;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oauth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(oauth2UserRequest);
        String provider = oauth2UserRequest.getClientRegistration()
            .getRegistrationId();
        System.out.println(">>> user service attributes: " + oauth2User.getAttributes());
        try {
            return service.upsert(
                provider,
                oauth2User.getName(),
                oauth2User.getAttributes()
            );
        } catch (AuthenticationNotSupportedException e) {
            throw new OAuth2AuthenticationException(
                null, e.getMessage(), e.getCause()
            );
        }
    }
}
