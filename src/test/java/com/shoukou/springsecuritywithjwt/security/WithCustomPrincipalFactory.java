package com.shoukou.springsecuritywithjwt.security;

import com.shoukou.springsecuritywithjwt.security.jwt.JwtAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;
import java.util.List;

/**
 * @apiNote https://docs.spring.io/spring-security/site/docs/5.2.0.RELEASE/reference/html/test.html
 */
public class WithCustomPrincipalFactory implements WithSecurityContextFactory<WithCustomPrincipal> {

    @Override
    public SecurityContext createSecurityContext(WithCustomPrincipal annotation) {

        SecurityContext context = SecurityContextHolder.createEmptyContext();

        GrantedAuthority authority = () -> "ROLE_USER";
        List<GrantedAuthority> authorities = Arrays.asList(authority);
        CustomPrincipal customPrincipal = CustomPrincipal.create(annotation.userId(), annotation.username());
        Object credentials = "";
        String issuer = "Mock";

        JwtAuthenticationToken authentication = new JwtAuthenticationToken(authorities, customPrincipal, credentials, issuer);

        context.setAuthentication(authentication);

        return context;
    }
}
