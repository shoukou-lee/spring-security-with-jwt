package com.shoukou.springsecuritywithjwt;

import com.shoukou.springsecuritywithjwt.user.UserDto;
import com.shoukou.springsecuritywithjwt.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@RequiredArgsConstructor
@Controller
public class IndexController {

    private final UserService userService;

    @GetMapping("")
    public String index() {
        return "index";
    }

    @GetMapping("/user")
    public @ResponseBody String user() {
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "manager";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(UserDto userDto) {
        log.info("dto.username : {}", userDto.getUsername());

        userService.join(userDto);

        return "redirect:/loginForm";
    }

    /**
     * SecurityConfig.@EnableGlobalMethodSecurity(securedEnabled = true)일 때,
     * 메서드 레벨에서 ROLE ACCESS LEVEL 지정 가능
     */
    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "개인정보";
    }

    /**
     * SecurityConfig.@EnableGlobalMethodSecurity(prePostEnabled = true)일 때,
     * 메서드 레벨에서 ROLE ACCESS LEVEL 지정 가능
     */
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "데이터";
    }
}
