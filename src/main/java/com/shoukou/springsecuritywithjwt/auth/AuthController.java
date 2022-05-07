package com.shoukou.springsecuritywithjwt.auth;

import com.shoukou.springsecuritywithjwt.jwt.JwtDto;
import com.shoukou.springsecuritywithjwt.user.dto.LoginDto;
import com.shoukou.springsecuritywithjwt.user.dto.SignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public JwtDto login(@RequestBody LoginDto loginDto) {
        return authService.login(loginDto);
    }

    @PostMapping("/signup")
    public JwtDto signUp(@RequestBody SignUpDto signUpDto) {
        return authService.signUp(signUpDto);
    }

}
