package com.shoukou.springsecuritywithjwt.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public UserListDto getUserList() {
        return userService.getUserList();
    }

    @GetMapping("/mydetail")
    public UserDto getMyDetail() {
        return userService.getMyDetail();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PostMapping("/ban/{userId}")
    public UserDto ban(@PathVariable Long userId) {
        return userService.ban(userId);
    }

}
