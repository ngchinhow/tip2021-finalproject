package com.tfip2021.module4.utils;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import static com.tfip2021.module4.models.Constants.REQUEST_STATE_PARAM_NAME;
import static com.tfip2021.module4.models.Constants.REQUEST_REDIRECT_URI_PARAM_NAME;

public class HttpServletRequestUtils {

    private HttpServletRequestUtils() {
        // Prevents an instantiation of a Utility class
        throw new IllegalStateException("Utility class");
    }

    public static String getState(HttpServletRequest request) {
        return request.getParameter(REQUEST_STATE_PARAM_NAME);
    }

    public static String getRedirectUri(HttpServletRequest request) {
        return request.getParameter(REQUEST_REDIRECT_URI_PARAM_NAME);
    }

    public static Optional<String> getBearerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer "))
            return Optional.of(bearerToken.split(" ")[1]);
        return Optional.empty();
    }
}
