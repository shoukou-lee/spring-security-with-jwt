package com.shoukou.springsecuritywithjwt.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RedisAccessTokenService {

    private final int ACC_EXP;
    private final RedisTemplate redisTemplate;

    public RedisAccessTokenService(RedisTemplate redisTemplate,
                                   @Value("${jwt.access.expiration}") int expiration) {
        this.redisTemplate = redisTemplate;
        this.ACC_EXP = expiration;
    }

    public void put(String accessToken) {
        ValueOperations<String, String> valOps = redisTemplate.opsForValue();
        valOps.set(accessToken, "logout", ACC_EXP);
        log.info("Redis 블랙리스트 등록");
    }

    public Boolean exists(String accessToken) {
        if (get(accessToken) == null) {
            return false;
        } else {
            return true;
        }
    }

    public void delete(String accessToken) {
        redisTemplate.delete(accessToken);
        log.info("Redis 블랙리스트 해제");
    }

    private String get(String accessToken) {
        ValueOperations<String, String> valOps = redisTemplate.opsForValue();
        return valOps.get(accessToken);
    }

}
