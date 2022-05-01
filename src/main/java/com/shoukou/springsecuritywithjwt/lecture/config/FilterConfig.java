package com.shoukou.springsecuritywithjwt.lecture.config;

import com.shoukou.springsecuritywithjwt.lecture.filter.CustomFilter;
import com.shoukou.springsecuritywithjwt.lecture.filter.CustomFilter2;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 기본적으로 Security filter가 Custom filter보다 먼저 실행됨 (단, addFilterBefore로 순서를 먼저 오게 설정 가능)
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<CustomFilter> filter1() {
        FilterRegistrationBean<CustomFilter> bean = new FilterRegistrationBean<>(new CustomFilter());
        bean.addUrlPatterns("/*"); // 모든 요청에 대해 필터 적용
        bean.setOrder(0); // 최우선순위의 필터 (낮은 숫자 우선)

        return bean;
    }

    @Bean
    public FilterRegistrationBean<CustomFilter2> filter2() {
        FilterRegistrationBean<CustomFilter2> bean = new FilterRegistrationBean<>(new CustomFilter2());
        bean.addUrlPatterns("/*"); // 모든 요청에 대해 필터 적용
        bean.setOrder(1); // 두번째 필터

        return bean;
    }

}
