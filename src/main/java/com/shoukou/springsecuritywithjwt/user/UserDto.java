package com.shoukou.springsecuritywithjwt.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class UserDto {

    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private Boolean isDeleted;

    public UserDto(User u) {
        this.id = u.getId();
        this.name = u.getName();
        this.email = u.getEmail();
        this.phoneNumber = u.getPhoneNumber();
        this.isDeleted = u.getIsDeleted();
    }
}
