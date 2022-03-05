package com.tfip2021.module4.services.disseminators;

import java.util.Map;

import javax.naming.AuthenticationNotSupportedException;

import com.tfip2021.module4.dto.SocialProvider;
import com.tfip2021.module4.models.DatabaseUser;

import org.springframework.stereotype.Service;

@Service
public class PropertiesDisseminatorFactory {

    private PropertiesDisseminatorFactory() { }

    public static DatabaseUser mergeUserWithAttributes(
        DatabaseUser user,
        String operation,
        String registrationId,
        String providerUserId,
        Map<String, Object> attributes
    ) throws AuthenticationNotSupportedException {
        PropertiesDisseminator disseminator;
        if (registrationId.equals(SocialProvider.GOOGLE.getLoginProvider())) {
            disseminator = new GooglePropertiesDisseminator(
                user,
                operation,
                registrationId,
                providerUserId,
                attributes
            );
        } else if (registrationId.equals(SocialProvider.LOCAL.getLoginProvider())) {
            disseminator = new LocalPropertiesDisseminator(
                user,
                operation,
                registrationId,
                providerUserId,
                attributes
            );
        }
        else {
            throw new AuthenticationNotSupportedException(
              "Authenticating via " + registrationId +
              " is currently not supported."  
            );
        }

        // return DatabaseUser with disseminated attributes
        return disseminator.disseminateAttributes();
    }

    public static String getUserEmail(
        String registrationId,
        Map<String, Object> attributes
    ) throws AuthenticationNotSupportedException {
        PropertiesDisseminator disseminator;
        if (registrationId.equals(SocialProvider.GOOGLE.getLoginProvider())) {
            disseminator = new GooglePropertiesDisseminator(attributes);
        } else if (registrationId.equals(SocialProvider.LOCAL.getLoginProvider())) {
            disseminator = new LocalPropertiesDisseminator(attributes);
        }
        else {
            throw new AuthenticationNotSupportedException(
                "Authenticating via " + registrationId +
                " is currently not supported."
            );
        }
        return disseminator.getEmail();
    }
}
