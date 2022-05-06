package com.shoukou.springsecuritywithjwt.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class UserListDto {
    private List<UserDto> userDtos;
}
