package com.shoukou.springsecuritywithjwt.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtIssuer {

    // custom private claims
    private final String USER_ID = "id";
    private final String USER_NAME = "uname";
    private final String USER_ROLE = "role";

    private final String accessKey;
    private final String refreshKey;
    private final int accessTokenExpirationInMinute;
    private final int refreshTokenExpirationInMinute;

    public JwtIssuer(@Value("${jwt.access.key}") String accessKey,
                     @Value("${jwt.refresh.key}") String refreshKey,
                     @Value("${jwt.access.expiration}") int accessTokenExpirationInMinute,
                     @Value("${jwt.refresh.expiration}") int refreshTokenExpirationInMinute) {
        this.accessKey = accessKey;
        this.refreshKey = refreshKey;
        this.accessTokenExpirationInMinute = accessTokenExpirationInMinute;
        this.refreshTokenExpirationInMinute = refreshTokenExpirationInMinute;
    }

    public String createAccessToken(Long userId, String username, String authority) {
        return createToken(userId, username, authority, this.accessKey, this.accessTokenExpirationInMinute);
    }

    public String createRefreshToken(Long userId, String username, String authority) {
        return createToken(userId, username, authority, this.refreshKey, this.refreshTokenExpirationInMinute);
    }

    private String createToken(Long userId, String username, String authority, String key, int expiration) {

        Claims claims = Jwts.claims();
        claims.put(USER_ID, userId.toString()); // 원하는 private claims를 골라 put
        claims.put(USER_NAME, username);
        claims.put(USER_ROLE, authority);

        Date now = new Date();
        Date expiredAt = new Date(now.getTime() + minuteToMillisecond(expiration));

        Key secretKey = Keys.hmacShaKeyFor(key.getBytes()); // signWith 메서드 매개변수로서 사용하기 위해 byte->ScreteKey 객체로 변환

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(key.equals(secretKey) ? "ACCESS" : "REFRESH")
                .setIssuer("SHOUKOU")
                .setIssuedAt(now)
                .setExpiration(expiredAt)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private int minuteToMillisecond(int minute) {
        return minute * 60 * 1000;
    }

}
