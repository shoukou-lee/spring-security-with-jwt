package com.shoukou.springsecuritywithjwt.security;

import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;

@Getter
@RedisHash
public class RefreshToken {

    @Id
    Long id;

    String refreshToken;

    Long userId;

    public RefreshToken(String refreshToken, Long userId) {
        this.refreshToken = refreshToken;
        this.userId = userId;
    }
}
