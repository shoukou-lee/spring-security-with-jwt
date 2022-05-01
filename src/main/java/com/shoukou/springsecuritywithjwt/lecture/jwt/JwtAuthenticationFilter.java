package com.shoukou.springsecuritywithjwt.lecture.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoukou.springsecuritywithjwt.lecture.auth.PrincipalDetails;
import com.shoukou.springsecuritywithjwt.lecture.user.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

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

        //TODO
        // username, password 받아서 정상인지 authenticationManager로 로그인 시도를 해본다
        // 그러면 PrincipalDetailService.loadUserByUsername() 이 자동으로 실행된다.
        // 권한 관리를 위해 PrincipalDetails (UserDetail 확장 커스텀 클래스) 를 세션에 담고
        // JWT를 만들어서 응답해준다.

        try {
            ObjectMapper om = new ObjectMapper(); // json parser 사용 - BufferedReader 로 일일히 line-breaking 해도 되긴 함 ..
            UserDto userDto = om.readValue(request.getInputStream(), UserDto.class); // inputStream 에 username/password 가 담겨있다

            log.info("userDto 파싱 결과 : {}", userDto); // override toString() required

            // 토큰 생성
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword());

            // 토큰을 통해 로그인 시도 -> PrincipalDetailService.loadUserByUsername() 실행됨
            // DB의 username/password와 일치하고 로그인이 정상적으로 되는 것을 확인하면 authentication 객체가 잘 생성됨
            Authentication authentication = authenticationManager.authenticate(token);

            // UserDetail의 값을 확인해보자
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            log.info("principalDetails.getUser() : {}", principalDetails.getUser());

            // authentication이 리턴되면서 세션 영역에 저장됨
            // 굳이 토큰을 쓰면서 세션을 만들 이유는 없지만, 귀찮은 권한 관리를 Spring security에 넘기기 위해
            return authentication;
        } catch (IOException e) {
            log.info("JSON 파싱 실패");
            e.printStackTrace();
        }

        return null;
    }

    // attempAuthentication 실행 후 인증 성공하면 실행되는 메서드
    // 여기서 JWT를 만들고 Requester에게 응답해준다
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("인증 완료, JWT 생성 !");

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        // token 생성
        String jwToken = JWT.create()
                .withSubject("shoukou-token")
                .withExpiresAt(new Date(System.currentTimeMillis() + (6 * 1000 * 1000)))
                // 넣고 싶은 key-value 를 withClaim() 에 넣는다
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512("shoukou-sign"));

        // Postman으로 로그인 요청 후 Response header에서 JWT를 확인해보자
        response.addHeader("Authorization", "Bearer " + jwToken);
    }
}
