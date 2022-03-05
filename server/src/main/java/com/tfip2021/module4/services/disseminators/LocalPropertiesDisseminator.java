package com.tfip2021.module4.services.disseminators;

import java.util.Map;

import com.tfip2021.module4.models.DatabaseUser;
import com.tfip2021.module4.models.DatabaseUser.DatabaseUserBuilder;

public class LocalPropertiesDisseminator extends PropertiesDisseminator {
    public LocalPropertiesDisseminator(Map<String, Object> attributes) {
        super(attributes);
    }

    public LocalPropertiesDisseminator(
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
        if (!this.isSupportedOperation())
            throw new IllegalArgumentException(
                "The provided operation " + this.getOperation() + 
                " is not supported yet!"
            );
        
        DatabaseUserBuilder builder = this.getUser().toBuilder()
            .displayName(this.getDisplayName());
        if (this.getOperation().equals("create"))
            builder = builder
                .provider(this.getProvider())
                .providerUserId(this.getProviderUserId())
                .username(this.getProviderUserId())
                .email(this.getEmail())
                .password(this.getPassword());
        return builder.build();        
    }

    public String getDisplayName() {
        return (String) this.getAttributes().get("displayName");
    }

    public String getPassword() {
        return (String) this.getAttributes().get("password");
    }
}
