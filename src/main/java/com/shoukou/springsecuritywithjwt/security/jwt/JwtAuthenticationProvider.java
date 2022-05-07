package com.shoukou.springsecuritywithjwt.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Slf4j
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final String secretKey;

    public JwtAuthenticationProvider(@Value("${jwt.access.key}") String secretKey) {
        this.secretKey = secretKey;
    }

    public Collection<? extends GrantedAuthority> createGrantedAuthority(Claims claims) {
        // private claim에서 Key가 role인 Value를 꺼내온다.
        String role = (String) claims.get("role");
        GrantedAuthority grantedAuthority = () -> role;

        List<GrantedAuthority> grantedAuthorities = Arrays.asList(grantedAuthority);

        return grantedAuthorities;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 받아온 JWT가 유효한지 검증하는 로직 구현
        // 검증 중 Exception이 발생하면 상위로 Throw 한다.

        // TODO : Exception 처리
        //  ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException

        log.info("secret:{}", secretKey);

        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Claims claims = parseJwt(token.getJwt());

        String subject = claims.getSubject();
        String userId = claims.get("id", String.class);
        String username = claims.get("uname", String.class);
        String issuer = claims.getIssuer();
        String credentials = "";

        log.info("subject : {}, credentials : {}", subject, credentials);

        Collection<? extends GrantedAuthority> grantedAuthorities = createGrantedAuthority(claims);

        log.info("An authenticated token has been issued !");
        log.info("private claims : {}, {}", userId, username);
        return new JwtAuthenticationToken(token.getJwt(), grantedAuthorities, subject, credentials, issuer, userId, username);
    }

    // JwtAuthenticationToken 클래스가 authentication 을 상속하는지 검사
    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public Long getUserIdFromExpiredToken(String jwt) {
        try {
            Claims claims = parseJwt(jwt);
        } catch (ExpiredJwtException e) {
            String id = e.getClaims().get("id", String.class);
            log.info("Extracted ID from expired token is {}", id);
            return Long.valueOf(id);
        }
        log.info("This token has not yet expired");
        throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
    }

    private Claims parseJwt(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }
}
