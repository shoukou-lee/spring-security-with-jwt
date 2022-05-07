package com.shoukou.springsecuritywithjwt.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
public class RedisRefreshTokenService {

    private final int refreshExpirationTimeInMinute;
    private final RedisTemplate redisTemplate;

    public RedisRefreshTokenService(RedisTemplate redisTemplate,
                                    @Value("${jwt.refresh.expiration}") int expiration) {
        this.redisTemplate = redisTemplate;
        this.refreshExpirationTimeInMinute = expiration;
    }

    @Transactional
    public void saveRefreshToken(Long userId, String refreshToken) {
        String id = userId.toString();
        ValueOperations<String, String> valOps = redisTemplate.opsForValue();
        valOps.set(id, refreshToken, Duration.ofMinutes(refreshExpirationTimeInMinute)); // 설정 시간 후 파기됨
    }

    @Transactional
    public void deleteRefreshToken(Long userId) {
        String id = userId.toString();
        redisTemplate.delete(id);
    }

    public String getRefreshToken(Long userId) {
        String id = userId.toString();
        ValueOperations<String, String> valOps = redisTemplate.opsForValue();
        String refreshToken = valOps.get(id);

        return refreshToken;
    }

    @Transactional
    public void updateRefreshToken(Long userId, String newToken) {
        deleteRefreshToken(userId);
        saveRefreshToken(userId, newToken);
    }

}
