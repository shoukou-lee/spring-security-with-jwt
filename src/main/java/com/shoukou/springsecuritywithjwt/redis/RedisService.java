package com.shoukou.springsecuritywithjwt.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisService {

    private final int refreshExpirationTimeInMinute;
    private final RedisTemplate redisTemplate;

    public RedisService(RedisTemplate redisTemplate,
                        @Value("${jwt.refresh.expiration}") int expiration) {
        this.redisTemplate = redisTemplate;
        this.refreshExpirationTimeInMinute = expiration;
    }

    public void saveRefreshToken(String refreshToken, Long userId) {

        String id = userId.toString();

        ValueOperations<String, String> valOps = redisTemplate.opsForValue();
        valOps.set(refreshToken, id, Duration.ofMinutes(refreshExpirationTimeInMinute));
    }

    public Long getRefreshToken(String refreshToken) {
        ValueOperations<String, String> valOps = redisTemplate.opsForValue();
        String id = valOps.get(refreshToken);

        return Long.valueOf(id);
    }

    public void deleteRefreshToken(String refreshToken) {
        redisTemplate.delete(refreshToken);
    }

}
