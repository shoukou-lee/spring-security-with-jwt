package com.shoukou.springsecuritywithjwt.lecture.controller;

import com.shoukou.springsecuritywithjwt.lecture.user.UserDto;
import com.shoukou.springsecuritywithjwt.lecture.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class RestApiController {

    private final UserService userService;

    @GetMapping("/home")
    public String home() {
        return "<h1>home</h1>";
    }

    @PostMapping("/reissue")
    public String reissue() {
        return "reissue";
    }

    @GetMapping("/user/{userId}")
    public String getUser(@PathVariable Long userId) {
        return "get" + String.valueOf(userId);
    }

    @GetMapping("/manager/reports")
    public String reports() {
        return "get reports";
    }

    @GetMapping("/admin/users")
    public String getUsers() {
        return "getUsers";
    }

    @PostMapping("/join")
    public String join(UserDto userDto) {
        log.info("dto.username : {}", userDto.getUsername());

        userService.join(userDto);

        return "회원가입 완료";
    }
}
