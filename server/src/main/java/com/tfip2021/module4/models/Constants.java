package com.tfip2021.module4.models;

import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
public class Constants {
    private Constants() {
        // Private constructor to prevent instantiating Constants
    }

    public static final long PACKAGE_SERIAL_VERSION_UID = 100000000001L;
    public static final String OAUTH2_AUTHORIZATION_REQUEST_REGISTRATION_ID_ATTRIBUTE_NAME = "registration_id";
    public static final String REQUEST_STATE_PARAM_NAME = OAuth2ParameterNames.STATE;
    public static final String REQUEST_REDIRECT_URI_PARAM_NAME = OAuth2ParameterNames.REDIRECT_URI;

    // Environment variables passed as Constants
    private static final String SERVER_DOMAIN = System.getenv("ENV_SERVER_DOMAIN");
    public static final String OAUTH2_BASE_URI = System.getenv("ENV_AUTH_OAUTH2_BASE_URI");
    public static final String BASIC_URI = SERVER_DOMAIN + System.getenv("ENV_AUTH_BASIC_BASE_URI");
    public static final String OAUTH2_URI = SERVER_DOMAIN + OAUTH2_BASE_URI;
    public static final String JWT_SECRET = System.getenv("ENV_JWT_SECRET");
    public static final long JWT_EXPIRATIONMILLISEC = 
        Long.parseLong(System.getenv("ENV_JWT_EXPIRATIONMILLISEC")
    );
}
