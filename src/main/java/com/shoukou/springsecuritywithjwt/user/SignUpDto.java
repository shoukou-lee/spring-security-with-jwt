package com.shoukou.springsecuritywithjwt.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class SignUpDto {
    private String name;
    private String email;
    private String phoneNumber;
}
