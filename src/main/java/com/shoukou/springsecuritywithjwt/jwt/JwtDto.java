package com.shoukou.springsecuritywithjwt.jwt;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtDto {
    private String accessToken;
    private String refreshToken;
    private String grantType;
}
