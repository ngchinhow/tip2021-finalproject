package com.tfip2021.module4.models;

import java.io.Serializable;

import static com.tfip2021.module4.models.Constants.PACKAGE_SERIAL_VERSION_UID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.gson.Gson;

import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
@Entity
@Table(name = "TRANSIENTOAUTH2AUTHORIZATIONREQUEST")
@Getter
@Setter
public class TransientOAuth2AuthorizationRequest implements Serializable {
    private static final long serialVersionUID = PACKAGE_SERIAL_VERSION_UID;

    @Id
    @Column(name = "STATE")
    private String state;
    
    @Column(name = "OAUTH2AUTHORIZATIONREQUEST", length = 5000)
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String oauth2AuthorizationRequest;

    @Column(name = "REDIRECTURI")
    private String redirectUri;

    public OAuth2AuthorizationRequest getOauth2AuthorizationRequest() {
        Gson gson = new Gson();
        return gson.fromJson(
            oauth2AuthorizationRequest,
            OAuth2AuthorizationRequest.class
        );
    }

    public void setOauth2AuthorizationRequest(
        OAuth2AuthorizationRequest oauth2AuthorizationRequest
    ) {
        Gson gson = new Gson();
        this.oauth2AuthorizationRequest = gson.toJson(oauth2AuthorizationRequest);
    }
}
