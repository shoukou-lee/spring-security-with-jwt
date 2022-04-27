package com.shoukou.springsecuritywithjwt.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CustomFilter3 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("필터 3");

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // 만약 올바른 토큰이 넘어오면 인증을 진행하고, 그렇지 않으면 컨트롤러 진입을 막는 부분을 여기서 구현할 수 있음
        // POST http://localhost:8080/login
        //        {
        //            "username" : "user",
        //            "password" : "user"
        //        }

        if (req.getMethod().equals("POST")) {
            log.info("POST 요청됨");
            String headerAuth = req.getHeader("Authorization");
            log.info("headerAuth : {}", headerAuth);

            if (headerAuth.equals("shoukou")) {
                log.info("인증 됨 !");
                chain.doFilter(req, res);
            } else {
                log.info("인증 안됨 !"); // 이때 CustomFilter 1, 2도 통과를 못한다 !
            }
        }
    }

}
