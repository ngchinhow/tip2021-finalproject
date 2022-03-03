package com.tfip2021.module4.models;

import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

public class Constants {
    public static final long PACKAGE_SERIAL_VERSION_UID = 100000000001L;
    public static final String OAUTH2_AUTHORIZATION_REQUEST_REGISTRATION_ID_ATTRIBUTE_NAME = "registration_id";
    public static final String AUTHORIZATION_REQUEST_BASE_URI = "/oauth2/authorization";
    public static final String REQUEST_STATE_PARAM_NAME = OAuth2ParameterNames.STATE;
    public static final String REQUEST_REDIRECT_URI_PARAM_NAME = OAuth2ParameterNames.REDIRECT_URI;
}
