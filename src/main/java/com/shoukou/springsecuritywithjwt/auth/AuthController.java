package com.shoukou.springsecuritywithjwt.auth;

import com.shoukou.springsecuritywithjwt.security.jwt.JwtDto;
import com.shoukou.springsecuritywithjwt.user.dto.LoginDto;
import com.shoukou.springsecuritywithjwt.user.dto.SignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public JwtDto login(@RequestBody LoginDto loginDto) {
        return authService.login(loginDto);
    }

    @GetMapping("/logout")
    public void logout() {
        authService.logout();
    }

    @PostMapping("/signup")
    public JwtDto signUp(@RequestBody SignUpDto signUpDto) {
        return authService.signUp(signUpDto);
    }

}
