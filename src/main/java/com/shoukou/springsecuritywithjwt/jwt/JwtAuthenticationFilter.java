package com.shoukou.springsecuritywithjwt.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * /login 요청이 오면 인터셉트 하는 필터
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // login 요청을 하면 로그인 시도를 위해 실행되는 메서드
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도 중 .. ");

        // username, password 받아서 정상인지 authenticationManager로 로그인 시도를 해본다
        // 그러면 PrincipalDetailService.loadUserByUsername() 이 실행된다.
        // PrincipalDetails를 세션에 담고 (권한 관리를 위해)
        // JWT를 만들어서 응답해준다.

        return super.attemptAuthentication(request, response);
    }
}
