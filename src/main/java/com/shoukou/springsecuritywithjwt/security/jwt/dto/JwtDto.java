package com.shoukou.springsecuritywithjwt.security.jwt.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtDto {
    private String accessToken;
    private String refreshToken;
    private String grantType;
}
