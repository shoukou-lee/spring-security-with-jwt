package com.shoukou.springsecuritywithjwt.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtIssuer {

    private final String USER_ID = "id";
    private final String USER_NAME = "uname";
    private final String USER_ROLE = "role";

    private final String secretKey;
    private final String refreshKey;
    private final int expirationTimeInMinute;
    private final int refreshExpirationTimeInMinute;

    public JwtIssuer(@Value("${jwt.secret.key}") String secretKey,
                     @Value("${jwt.refresh.key}") String refreshKey,
                     @Value("${jwt.secret.expiration}") int expirationTimeInMinute,
                     @Value("${jwt.refresh.expiration}") int refreshExpirationTimeInMinute) {
        this.secretKey = secretKey;
        this.refreshKey = refreshKey;
        this.expirationTimeInMinute = expirationTimeInMinute;
        this.refreshExpirationTimeInMinute = refreshExpirationTimeInMinute;
    }

    public String createAccessToken(Long userId, String username, String authority) {
        return createToken(userId, username, authority, this.secretKey, this.expirationTimeInMinute);
    }

    public String createRefreshToken(Long userId, String username, String authority) {
        return createToken(userId, username, authority, this.refreshKey, this.refreshExpirationTimeInMinute);
    }

    private String createToken(Long userId, String username, String authority, String key, int expiration) {

        Claims claims = Jwts.claims();
        claims.setSubject(userId.toString()); // subject로는 userId를 사용
        claims.put(USER_NAME, username); // 원하는 private claims를 put
        claims.put(USER_ROLE, authority); // 원하는 private claims를 put

        Date now = new Date();
        Date expiredAt = new Date(now.getTime() + minuteToMillisecond(expiration));

        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredAt)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private int minuteToMillisecond(int minute) {
        return minute * 60 * 1000;
    }

}
