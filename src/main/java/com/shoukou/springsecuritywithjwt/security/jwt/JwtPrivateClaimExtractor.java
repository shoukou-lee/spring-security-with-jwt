package com.shoukou.springsecuritywithjwt.security.jwt;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtPrivateClaimExtractor {

    public static String getToken() {
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        return authentication.getJwt();
    }

    public static Long getUserId() {


        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        log.info("SecurityContextHolder에서 꺼내온 유저 아이디 : {}", authentication.getUserId());

        return Long.valueOf(authentication.getUserId());
    }

    public static String getUsername() {
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return authentication.getUsername();
    }

}
