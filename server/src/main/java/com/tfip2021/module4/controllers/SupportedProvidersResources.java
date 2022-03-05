package com.tfip2021.module4.controllers;

import static com.tfip2021.module4.models.Constants.BASIC_URI;
import static com.tfip2021.module4.models.Constants.OAUTH2_URI;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import com.tfip2021.module4.dto.SocialProvider;
import com.tfip2021.module4.dto.SupportedProvider;
import com.tfip2021.module4.dto.SupportedProvidersResponse;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/auth")
public class SupportedProvidersResources {
    @GetMapping(
        path = "/supported_providers",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SupportedProvidersResponse> getSupportedProviders() {
        SupportedProvidersResponse response = new SupportedProvidersResponse();
        List<SupportedProvider> socialProviders = new LinkedList<>();
        Stream.of(
            SocialProvider.class.getEnumConstants()
        )
        .forEach(provider -> {
            String providerName = provider.getLoginProvider();
            if (provider == SocialProvider.LOCAL) {
                response.setLocal(
                    SupportedProvider.builder()
                        .provider(providerName)
                        .signUpUri(BASIC_URI + "/signup")
                        .loginUri(BASIC_URI + "/login")
                        .build()
                );
            } else {
                socialProviders.add(
                    SupportedProvider.builder()
                        .provider(providerName)
                        .imageUri("/assets/img/social_providers/" + providerName + ".png")
                        .imageAlt("Sign in with " + StringUtils.capitalize(providerName))
                        .loginUri(OAUTH2_URI + "/" + providerName)
                        .build() 
                );
            }
        });
        response.setSocial(socialProviders);
        return ResponseEntity.ok(response);
    }
}
