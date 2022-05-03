package com.shoukou.springsecuritywithjwt.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final byte[] secretKeyByte;

    public JwtAuthenticationProvider(@Value("${jwt.secret}") String secret) {
        this.secretKeyByte = secret.getBytes();
    }

    public Collection<? extends GrantedAuthority> createGrantedAuthority(Claims claims) {
        // 클레임에서 Key가 role인 Value들을 꺼내온다.
        // AbstractAuthenticationToken 생성자 인자로 Collection<?...> 가 필요하다 ... Multiple-role을 고려한 듯

        List<String> roles = (List) claims.get("roles");
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        for (String role : roles) {
            GrantedAuthority grantedAuthority = () -> role; // 람다식 축약 형태, new GrantedAuthority() {@Override getAuthority} 를 의미
            grantedAuthorities.add(grantedAuthority);
        }

        return grantedAuthorities;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 받아온 JWT가 유효한지 검증하는 로직 구현
        // 검증 중 Exception이 발생하면 상위로 Throw 한다.
        // ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException

        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKeyByte)
                .build()
                .parseClaimsJws(token.getJwt())
                .getBody();

        String subject = claims.getSubject();
        String credentials = "";
        Collection<? extends GrantedAuthority> grantedAuthorities = createGrantedAuthority(claims);

        return new JwtAuthenticationToken(subject, credentials, grantedAuthorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // JwtAuthenticationToken 클래스가 authentication 인자 타입을 지원한다면 true, 그렇지 않다면 false를 리턴
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
