package com.tfip2021.module4.services.mappers.user;

import java.util.Map;

import javax.naming.AuthenticationNotSupportedException;

import com.tfip2021.module4.dto.SocialProvider;
import com.tfip2021.module4.models.DatabaseUser;

import org.springframework.util.StringUtils;

public class UserMapperFactory {

    private UserMapperFactory() { }

    public static DatabaseUser mergeUserWithAttributes(
        DatabaseUser user,
        String operation,
        String registrationId,
        String providerUserId,
        Map<String, Object> attributes
    ) throws AuthenticationNotSupportedException {
        UserMapper mapper;
        if (registrationId.equals(SocialProvider.GOOGLE.getLoginProvider())) {
            mapper = new GoogleUserMapper(
                user,
                operation,
                registrationId,
                providerUserId,
                attributes
            );
        } else if (registrationId.equals(SocialProvider.LOCAL.getLoginProvider())) {
            mapper = new LocalUserMapper(
                user,
                operation,
                registrationId,
                providerUserId,
                attributes
            );
        }
        else {
            throw new AuthenticationNotSupportedException(
              "Authenticating via " + StringUtils.capitalize(registrationId) +
              " is currently not supported."  
            );
        }

        // return DatabaseUser with disseminated attributes
        return mapper.disseminateAttributes();
    }

    public static String getUserEmail(
        String registrationId,
        Map<String, Object> attributes
    ) throws AuthenticationNotSupportedException {
        UserMapper mapper;
        if (registrationId.equals(SocialProvider.GOOGLE.getLoginProvider())) {
            mapper = new GoogleUserMapper(attributes);
        } else if (registrationId.equals(SocialProvider.LOCAL.getLoginProvider())) {
            mapper = new LocalUserMapper(attributes);
        }
        else {
            throw new AuthenticationNotSupportedException(
                "Authenticating via " + registrationId +
                " is currently not supported."
            );
        }
        return mapper.getEmail();
    }
}
