package com.shoukou.springsecuritywithjwt.config;

import com.shoukou.springsecuritywithjwt.filter.CustomFilter;
import com.shoukou.springsecuritywithjwt.filter.CustomFilter3;
import com.shoukou.springsecuritywithjwt.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;

/**
 * 이 Configuration을 오버라이딩 하면, Spring security가 /loginForm.html 을 인터셉트 하지 않는다
 */
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 애노테이션 활성화, preAuthorize 애노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // JWT 사용 위해 세션, 기존 form login, http 로그인 방식을 모두 안씀
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(new CustomFilter3(), SecurityContextPersistenceFilter.class);

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 사용 안하는 옵션
                .and()
                .addFilter(corsFilter) // CorsConfig에서 등록한 Filter 빈을 추가, Security filter에 등록 인증 (CrossOrigin 인증 X)
                .formLogin().disable() // 기존의 form Login을 더이상 쓰지 않을 것
                .httpBasic().disable() // HTTP Basic 인증 방식 또한 안쓸 것 (Bearer 방식을 사용하기 위해)
                .addFilter(new JwtAuthenticationFilter(authenticationManager())) // formLogin이 disable이므로, /login을 인터셉트하기 위한 필터 추가
                .authorizeRequests()
                .antMatchers("/api/v1/manager/**")
                .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admin/**")
                .access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll();
    }

}
