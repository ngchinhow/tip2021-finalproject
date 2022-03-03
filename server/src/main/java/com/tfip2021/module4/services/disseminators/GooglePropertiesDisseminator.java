package com.tfip2021.module4.services.disseminators;

import java.util.Map;

import com.tfip2021.module4.models.DatabaseUser;
import com.tfip2021.module4.models.DatabaseUser.DatabaseUserBuilder;

import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

public class GooglePropertiesDisseminator extends PropertiesDisseminator {

    public GooglePropertiesDisseminator(Map<String, Object> attributes) {
        super(attributes);
    }

    public GooglePropertiesDisseminator(
        DatabaseUser user,
        String operation,
        String provider,
        String providerUserId,
        Map<String, Object> attributes
    ) {
        super(user, operation, provider, providerUserId, attributes);
    }
    
    @Override
    public DatabaseUser disseminateAttributes() {
        if (
            !this.getOperation().equals("create")
            && !this.getOperation().equals("update")
        )
            throw new IllegalArgumentException(
                "The provided operation " + this.getOperation() + 
                " is not supported yet!"
            );
        DatabaseUserBuilder builder = 
            this.getUser().toBuilder()
                .displayName(this.getDisplayName())
                .profilePictureUrl(this.getProfilePictureUrl())
                .attributes(this.getAttributes())
                .idToken(this.getIdToken())
                .userInfo(this.getUserInfo());
        if (this.getOperation().equals("create"))
            builder = builder
                .provider(this.getProvider())
                .providerUserId(this.getProviderUserId())
                .username(this.getProviderUserId())
                .email(this.getEmail());
        return builder.build();
    }

    public String getDisplayName() {
        return (String) this.getAttributes().get("name");
    }

    @Override
    public String getEmail() {
        return (String) this.getAttributes().get("email");
    }

    public String getProfilePictureUrl() {
        return (String) this.getAttributes().get("picture");
    }

    public OidcIdToken getIdToken() {
        return (OidcIdToken) this.getAttributes().get("idToken");
    }

    public OidcUserInfo getUserInfo() {
        return (OidcUserInfo) this.getAttributes().get("userInfo");
    }
}
