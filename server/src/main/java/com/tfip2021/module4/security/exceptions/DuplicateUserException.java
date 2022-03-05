package com.tfip2021.module4.security.exceptions;

import static com.tfip2021.module4.models.Constants.PACKAGE_SERIAL_VERSION_UID;
import org.springframework.security.core.AuthenticationException;

public class DuplicateUserException extends AuthenticationException {
    public static final long serialVersionUID = PACKAGE_SERIAL_VERSION_UID;

    public DuplicateUserException(final String message) {
        super(message);
    }
    
}
