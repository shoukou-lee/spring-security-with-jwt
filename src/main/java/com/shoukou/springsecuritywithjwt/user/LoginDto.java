package com.shoukou.springsecuritywithjwt.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class LoginDto {
    private String username;
}
