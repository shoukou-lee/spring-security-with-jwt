package com.shoukou.springsecuritywithjwt.security;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @apiNote https://docs.spring.io/spring-security/site/docs/5.2.0.RELEASE/reference/html/test.html
 */
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithCustomPrincipalFactory.class)
public @interface WithCustomPrincipal {

    long userId() default 1;

    String username() default "MockUser";

}
