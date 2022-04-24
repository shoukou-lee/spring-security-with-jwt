package com.shoukou.springsecuritywithjwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 이 Configuration을 오버라이딩 하면, Spring security가 /loginForm.html 을 인터셉트 하지 않는다
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 애노테이션 활성화, preAuthorize 애노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated() // /user..는 인증 필요
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')") // 인증 및 권한 체크
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')") // 인증 및 권한 체크
                .anyRequest().permitAll() // 이에외는 모두 허용
                .and().formLogin().loginPage("/loginForm")
                // .usernameParameter("somethingElseUserName) // loginForm의 username을 바꿀 때
                .loginProcessingUrl("/login") // /login이 호출이 되면 security가 인터셉트 해서 대신 로그인
                .defaultSuccessUrl("/"); // 로그인 성공 시 메인 페이지로 이동
    }

}
