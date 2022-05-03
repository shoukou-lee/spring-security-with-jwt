package com.shoukou.springsecuritywithjwt.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final String AUTHORIZATION = "Authorization";
    private final String BEARER = "Bearer ";

    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //TODO
        // Exception을 어디서 처리할지 고민해보자 ..
        // 상위 메서드로 올린다면 어디에서 처리되는건지 ...

        String jwt = parseHeader(request);

        Authentication jwtAuthenticationToken = new JwtAuthenticationToken(jwt);
        Authentication authenticate = authenticationManager.authenticate(jwtAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        filterChain.doFilter(request, response); // invoke next filter in the filter chain
    }

    private String parseHeader(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);

        if (header == null || !header.startsWith(BEARER)) {
            throw new RuntimeException("토큰 검증 실패");
        }
        return header.replace(BEARER, "");
    }
}
