package com.shoukou.springsecuritywithjwt.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/auth")
    public String auth() {
        return "auth";
    }

    @GetMapping("/user")
    public String user() {
        return "user";
    }

    @GetMapping("/manager")
    public String manager() {
        return "manager";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

}
