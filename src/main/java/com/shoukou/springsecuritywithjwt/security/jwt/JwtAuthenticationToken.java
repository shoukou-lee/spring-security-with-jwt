package com.shoukou.springsecuritywithjwt.security.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

//TODO : Authentication의 기본 구현체인 AbstractAuthenticationToken을 확장해서 JWT를 만든다
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private String jwt;
    private Object principal;
    private Object credentials;

    // 인증 안된 토큰 발급
    public JwtAuthenticationToken(String jwt) {
        super(null);
        this.jwt = jwt;
        this.setAuthenticated(false);
    }

    // 인증된 토큰 발급
    public JwtAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        this.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    public String getJwt() {
        return this.jwt;
    }
}
