package com.shoukou.springsecuritywithjwt.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String phoneNumber;

    private Boolean isDeleted;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public User(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = UserRole.ROLE_USER;
        this.isDeleted = false;
    }

    public User softDelete() {
        this.isDeleted = true;
        return this;
    }
}
