package com.shoukou.springsecuritywithjwt.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UserListDto {
    private List<UserDto> userDtos;
}
