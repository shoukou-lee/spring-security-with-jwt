package com.shoukou.springsecuritywithjwt.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class SignUpDto {
    private String name;
    private String email;
    private String phoneNumber;
}
