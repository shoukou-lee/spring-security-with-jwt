package com.shoukou.springsecuritywithjwt.user;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String password;
    private String email;
    private UserRole role;

    @CreationTimestamp
    LocalDateTime createdAt;

    public User(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }
}
