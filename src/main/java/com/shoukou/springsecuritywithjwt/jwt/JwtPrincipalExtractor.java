package com.shoukou.springsecuritywithjwt.jwt;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtPrincipalExtractor {

    public static Long getUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("SecurityContextHolder에서 꺼내온 유저 아이디 : {}", authentication.getName());

        return Long.valueOf(authentication.getPrincipal().toString());
    }

}
