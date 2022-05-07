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

    private final String secretKey;
    private final String refreshKey;
    private final int expirationTimeInMinute;
    private final int refreshExpirationTimeInMinute;

    public JwtIssuer(@Value("${jwt.access.key}") String secretKey,
                     @Value("${jwt.refresh.key}") String refreshKey,
                     @Value("${jwt.access.expiration}") int expirationTimeInMinute,
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
        claims.put(USER_ID, userId.toString());
        claims.put(USER_NAME, username); // 원하는 private claims를 put
        claims.put(USER_ROLE, authority); // 원하는 private claims를 put

        Date now = new Date();
        Date expiredAt = new Date(now.getTime() + minuteToMillisecond(expiration));

        Key secretKey = Keys.hmacShaKeyFor(key.getBytes());

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
