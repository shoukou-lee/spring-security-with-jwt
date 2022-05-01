package com.shoukou.springsecuritywithjwt.lecture.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.shoukou.springsecuritywithjwt.lecture.auth.PrincipalDetails;
import com.shoukou.springsecuritywithjwt.lecture.user.User;
import com.shoukou.springsecuritywithjwt.lecture.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//TODO
// 권한 인증이 필요한 URL 요청은 BasicAuthenticationFilter를 거쳐야 함

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("권한 인증이 필요한 요청이 들어옴");

        String jwtHeader = request.getHeader("Authorization");
        log.info("jwtHeader = {}", jwtHeader);

        // 헤더가 있는 지 확인
        if (jwtHeader == null || !jwtHeader.startsWith("Bearer")) {
            log.info("헤더 인증 실패");
            chain.doFilter(request, response);
            return;
        }

        log.info("헤더 인증 성공");

        // JWT 검증해서 정상적인 사용자로부터의 요청인지 확인
        log.info("JWT 검증");
        String jwt = request.getHeader("Authorization").replace("Bearer ", "");
        String username = JWT.require(Algorithm.HMAC512("shoukou-sign"))
                .build()
                .verify(jwt) // 서명하기
                .getClaim("username") // username 가져오기
                .asString();

        // 서명이 잘 됐다면 username != null
        if (username != null) {
            log.info("서명 완료");
            User user = userRepository.findByUserName(username)
                    .orElseThrow(() -> new RuntimeException("유저 찾기 실패"));

            PrincipalDetails principalDetails = new PrincipalDetails(user);

            // JWT 서명이 정상이면 Authentication 객체를 강제로 만들어준다. (비밀번호는 null로 비워둠)
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

            // 강제로 Security 세션에 접근 후 Authentication 객체 저장
            SecurityContextHolder.getContext()
                    .setAuthentication(authentication); // 세션 공간

            // Authorization 완료 ! 이후 남은 필터 계속 진행
            chain.doFilter(request, response);
            log.info("Authorization 끝 !");
        }

    }

}
