package com.shoukou.springsecuritywithjwt.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisRefreshTokenService {

    private final int REF_EXP;
    private final RedisTemplate redisTemplate;

    public RedisRefreshTokenService(RedisTemplate redisTemplate,
                                    @Value("${jwt.refresh.expiration}") int expiration) {
        this.redisTemplate = redisTemplate;
        this.REF_EXP = expiration;
    }

    public void saveRefreshToken(Long userId, String refreshToken) {
        String id = userId.toString();
        ValueOperations<String, String> valOps = redisTemplate.opsForValue();
        valOps.set(id, refreshToken, Duration.ofMinutes(REF_EXP)); // 설정 시간 후 파기됨
    }

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

    public void updateRefreshToken(Long userId, String newToken) {
        deleteRefreshToken(userId);
        saveRefreshToken(userId, newToken);
    }

}
