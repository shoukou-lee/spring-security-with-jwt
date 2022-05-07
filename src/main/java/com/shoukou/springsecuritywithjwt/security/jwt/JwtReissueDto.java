package com.shoukou.springsecuritywithjwt.security.jwt;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtReissueDto {
    private String accessToken;
    private String refreshToken;
}
