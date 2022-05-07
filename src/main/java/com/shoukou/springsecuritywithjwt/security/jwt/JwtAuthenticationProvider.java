package com.shoukou.springsecuritywithjwt.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Slf4j
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final String secretKey;

    public JwtAuthenticationProvider(@Value("${jwt.secret.key}") String secretKey) {
        this.secretKey = secretKey;
    }

    public Collection<? extends GrantedAuthority> createGrantedAuthority(Claims claims) {
        // private claim에서 Key가 role인 Value를 꺼내온다.
        //TODO
        // AbstractAuthenticationToken 생성자 인자로 Collection<?...> 가 필요하다 ... Multiple-role을 고려한 듯
        // 나는 single-role만 필요한데, 우선 String -> GrantedAuthority -> Arrays.asList(..)로 변환하자 ..
        // 나중에 Token 생성자 인자를 수정하는게 좋을지도 ..

        String role = (String) claims.get("role");
        GrantedAuthority grantedAuthority = () -> role;

        List<GrantedAuthority> grantedAuthorities = Arrays.asList(grantedAuthority);

        return grantedAuthorities;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 받아온 JWT가 유효한지 검증하는 로직 구현
        // 검증 중 Exception이 발생하면 상위로 Throw 한다.
        // ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException

        log.info("secret:{}", secretKey);

        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token.getJwt())
                .getBody();

        String subject = claims.getSubject();
        String credentials = "";

        log.info("subject : {}, credentials : {}", subject, credentials);

        Collection<? extends GrantedAuthority> grantedAuthorities = createGrantedAuthority(claims);

        log.info("인증된 토큰 발급 !");
        return new JwtAuthenticationToken(subject, credentials, grantedAuthorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // JwtAuthenticationToken 클래스가 authentication 인자 타입을 지원한다면 true, 그렇지 않다면 false를 리턴
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
