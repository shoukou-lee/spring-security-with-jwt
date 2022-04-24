package com.shoukou.springsecuritywithjwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 내 서버가 응답할 때 JSON을 JS로 처리할 수 있게 할지를 설정
        config.addAllowedOrigin("*"); // 모든 IP에 응답 허용
        config.addAllowedHeader("*"); // 모든 헤더에 응답 허용
        config.addAllowedMethod("*"); // 모든 HTTP 메서드의 응답 허용
        source.registerCorsConfiguration("/api/**", config); // /api/**로 들어오는 요청에 대해, 이 Config를 등록
        return new CorsFilter(source);
    }

}
