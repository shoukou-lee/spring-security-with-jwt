package com.shoukou.springsecuritywithjwt;

import com.shoukou.springsecuritywithjwt.user.User;
import com.shoukou.springsecuritywithjwt.user.UserDto;
import com.shoukou.springsecuritywithjwt.user.UserRepository;
import com.shoukou.springsecuritywithjwt.user.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


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

        User user = new User(userDto.getUsername(), userDto.getPassword(), userDto.getEmail());
        user.setRole(UserRole.ROLE_USER);

        /**
        패스워드 encryption이 없으면 시큐리티 로그인이 불가능
         WARN 8585 ---BCryptPasswordEncoder: Encoded password does not look like BCrypt
         */
        String rawPassword = userDto.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);

        userRepository.save(user);

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
