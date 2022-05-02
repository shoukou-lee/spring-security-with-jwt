package com.shoukou.springsecuritywithjwt.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

//TODO : Authentication의 기본 구현체인 AbstractAuthenticationToken을 확장해서 JWT를 만든다
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private String jwt;
    private Object principal;
    private Object credentials;

    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal
     *                    represented by this authentication object.
     */
    public JwtAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
    }

    @Override
    public Object getCredentials() {
        //TODO : 실제 구현 필요
        return null;
    }

    @Override
    public Object getPrincipal() {
        //TODO : 실제 구현 필요
        return null;
    }

    public String getJwt() {
        return this.jwt;
    }
}
