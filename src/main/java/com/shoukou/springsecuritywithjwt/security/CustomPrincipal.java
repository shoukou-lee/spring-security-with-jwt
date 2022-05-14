package com.shoukou.springsecuritywithjwt.security;

import lombok.Getter;

@Getter
public class CustomPrincipal {

    // private claims
    private Long userId;
    private String username;

    private CustomPrincipal(Long userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public static CustomPrincipal create(Long userId, String username) {
        return new CustomPrincipal(userId, username);
    }

}
