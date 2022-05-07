package com.shoukou.springsecuritywithjwt.security.jwt.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtReissueDto {
    private String accessToken;
    private String refreshToken;
}
