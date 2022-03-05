package com.tfip2021.module4.security.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.tfip2021.module4.dto.SignUpRequest;

public class PasswordConstraintValidator implements 
    ConstraintValidator<ValidPassword, SignUpRequest> {

    @Override
    public boolean isValid(
        final SignUpRequest signupRequest,
        ConstraintValidatorContext context
    ) {
        return signupRequest.getPassword()
            .equals(signupRequest.getMatchingPassword());
    }
}
