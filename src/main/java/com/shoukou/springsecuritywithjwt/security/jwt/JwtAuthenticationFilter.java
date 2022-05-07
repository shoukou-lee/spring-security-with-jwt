package com.shoukou.springsecuritywithjwt.security.jwt;

import com.shoukou.springsecuritywithjwt.redis.RedisAccessTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
    OncePerRequestFilter : 요청에 대해 단 한번의 실행이 보장되는 필터
    doFilterInternal(...) 메서드가 추상 메서드로 선언되어 있고,이를 구현해야 함
    ** 구현 내용 **
    AuthenticationManager에게 토큰 검증을 요청한다. 검증 결과가 문제 없으면 SecurityContextHolder에 저장한다.
    lecture의 JwtAuthorizationFilter 참고
 */

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final String AUTHORIZATION = "Authorization";
    private final String BEARER = "Bearer ";

    private final AuthenticationManager authenticationManager;
    private final RedisAccessTokenService redisAccessTokenService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, RedisAccessTokenService redisAccessTokenService) {
        this.authenticationManager = authenticationManager;
        this.redisAccessTokenService = redisAccessTokenService;
    }

    /**
     * 로그인/회원가입 요청은 헤더의 Authorization에 맞는 토큰이 없더라도 Auth-filtered 되지 않아야 함
     * parseHeader에서 토큰이 없다고 무작정 Exception을 터뜨리지 않고, null을 리턴한 뒤 다음 필터를 타게 하자
     * https://stackoverflow.com/questions/46068433/spring-security-with-filters-permitall-not-working
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //TODO
        // Exception을 어디서 처리할지 고민해보자 ..
        // 상위 메서드로 올린다면 어디에서 처리되는건지 ...

        String jwt = parseHeader(request);

        // 로그아웃된 사용자의 토큰인데 요청이 들어오는 경우를 확인
        if (redisAccessTokenService.exists(jwt)) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "This token has already been logged out and is no longer valid. ");
        }

        if (jwt != null) {
            try {
                Authentication jwtAuthenticationToken = new JwtAuthenticationToken(jwt);
                Authentication authenticate = authenticationManager.authenticate(jwtAuthenticationToken);

                log.info("JWT 받아와서 SecurityContextHolder에 넣자 ! ");
                SecurityContextHolder.getContext().setAuthentication(authenticate);

            } catch (AuthenticationException authenticationException) {
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response); // invoke next filter in the filter chain
    }

    private String parseHeader(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);

        if (header == null || !header.startsWith(BEARER)) {
            log.info("토큰 검증 실패");
            return null;
        }
        return header.replace(BEARER, "");
    }
}
