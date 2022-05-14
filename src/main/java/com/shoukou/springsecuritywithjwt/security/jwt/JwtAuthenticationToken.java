package com.shoukou.springsecuritywithjwt.security.jwt;

import com.shoukou.springsecuritywithjwt.security.CustomPrincipal;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

// Authentication의 기본 구현체인 AbstractAuthenticationToken을 확장해서 JWT를 만든다
@Getter
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private String jwt;
    private CustomPrincipal principal;
    private Object credentials;

    // registered claim
    private String issuer;

    // 인증 안된 토큰 발급
    public JwtAuthenticationToken(String jwt) {
        super(null);
        this.jwt = jwt;
        this.setAuthenticated(false);
    }

    // 인증된 토큰 발급
    public JwtAuthenticationToken(Collection<? extends GrantedAuthority> authorities, Object principal, Object credentials, String issuer) {
        super(authorities);
        this.principal = (CustomPrincipal) principal;
        this.credentials = credentials;
        this.issuer = issuer;
        this.setAuthenticated(true);
    }

}
