package com.tfip2021.module4.dto;

public enum SocialProvider {
    GOOGLE("google"), LOCAL("local");

    private String loginProvider;

    public String getLoginProvider() {
        return loginProvider;
    }

    SocialProvider(final String loginProvider) {
        this.loginProvider = loginProvider;
    }
}
