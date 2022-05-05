package com.shoukou.springsecuritywithjwt.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

//TODO : Authentication의 기본 구현체인 AbstractAuthenticationToken을 확장해서 JWT를 만든다
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private String jwt;
    private Object principal;
    private Object credentials;

    public JwtAuthenticationToken(String jwt) {
        super(null);
        this.jwt = jwt;
        this.setAuthenticated(false);
    }

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
