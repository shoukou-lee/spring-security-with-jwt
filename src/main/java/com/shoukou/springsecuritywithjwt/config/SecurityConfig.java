package com.shoukou.springsecuritywithjwt.config;

import com.shoukou.springsecuritywithjwt.jwt.JwtAuthenticationFilter;
import com.shoukou.springsecuritywithjwt.jwt.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationProvider jwtAuthenticationProvider; // AuthenticationProvider의 커스텀 구현체

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
//                .antMatchers("/users/**").hasAnyRole("ROLE_USER", "ROLE_MANAGER", "ROLE_ADMIN")
//                .antMatchers("/manager/**").hasAnyRole("ROLE_MANAGER", "ROLE_ADMIN")
//                .antMatchers("/admin/**").hasAnyRole("ROLE_ADMIN")
                .anyRequest().permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //TODO
        // Security filter chain 구조 그림 보면 LogoutFilter 이후에 JWTFilter가 오는게 맞아 보이는데, 확실한가 .. ?

        // http.addFilter(new JwtAuthenticationFilter(authenticationManager));

        AuthenticationManager authManager = authenticationManager();
        http.addFilterAfter(new JwtAuthenticationFilter(authManager), LogoutFilter.class);

    }

}
