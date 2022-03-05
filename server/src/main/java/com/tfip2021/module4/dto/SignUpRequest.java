package com.tfip2021.module4.dto;

import lombok.Data;

@Data
public class SignUpRequest {
    private String displayName;
    private String email;
    private String password;
    private String matchingPassword;
}
