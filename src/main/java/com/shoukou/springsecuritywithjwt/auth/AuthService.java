package com.shoukou.springsecuritywithjwt.auth;

import com.shoukou.springsecuritywithjwt.redis.RedisAccessTokenService;
import com.shoukou.springsecuritywithjwt.redis.RedisRefreshTokenService;
import com.shoukou.springsecuritywithjwt.security.jwt.*;
import com.shoukou.springsecuritywithjwt.security.jwt.dto.JwtDto;
import com.shoukou.springsecuritywithjwt.security.jwt.dto.JwtReissueDto;
import com.shoukou.springsecuritywithjwt.user.User;
import com.shoukou.springsecuritywithjwt.user.UserRepository;
import com.shoukou.springsecuritywithjwt.user.dto.LoginDto;
import com.shoukou.springsecuritywithjwt.user.dto.SignUpDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AuthService {

    private final RedisAccessTokenService redisAccessTokenService;
    private final RedisRefreshTokenService redisRefreshTokenService;
    private final UserRepository userRepository;
    private final JwtIssuer jwtIssuer;
    private final JwtAuthenticationProvider provider;

    public JwtDto login(LoginDto loginDto) {

        User user = userRepository.findByUsername(loginDto.getName())
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "user not found"));

        return createJwt(user);
    }

    public void logout() {
        String token = JwtPrivateClaimExtractor.getToken();
        Long userId = JwtPrivateClaimExtractor.getUserId();
        redisRefreshTokenService.deleteRefreshToken(userId);
        SecurityContextHolder.clearContext();

        redisAccessTokenService.put(token); // 블랙리스트에 올려두기

        log.info("Successfully logged out !");
    }

    /**
     * https://stackoverflow.com/questions/32060478/is-a-refresh-token-really-necessary-when-using-jwt-token-authentication
     * - 고민해볼 점들 -
     * 각 유저에 대해 Access-Refresh token pair을 저장한다.
     * 공격자가 Refresh 토큰을 탈취해 새 Access token을 발급 받았다고 하자.
     * 서버에서는 Access token이 만료되지 않았는데, 재발급 요청이 있는 경우 탈취라고 판단하고 만료시킨다.
     * 정상 유저와 공격자 모두 토큰이 만료되어 재발급 받아야 한다.
     */
    public JwtDto reissue(JwtReissueDto jwtReissueDto) {

        Long userIdInToken = provider.getUserIdFromExpiredToken(jwtReissueDto.getAccessToken());
        String refreshTokenInRedis = redisRefreshTokenService.getRefreshToken(userIdInToken);

        if (refreshTokenInRedis.equals(jwtReissueDto.getRefreshToken())) {
            // 새 토큰 발급
            User user = userRepository.findById(userIdInToken)
                    .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "User not found"));

            JwtDto jwt = createJwt(user);

            redisRefreshTokenService.updateRefreshToken(user.getId(), jwt.getRefreshToken());

            return jwt;
        }

        throw new RuntimeException("Invalid Refresh token");
    }

    @Transactional
    public JwtDto signUp(SignUpDto signUpDto) {

        boolean userNameExists = userRepository.findByUsername(signUpDto.getName()).isPresent();
        if (userNameExists) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "User name already exists");
        }

        boolean phoneNumberExists = userRepository.findByPhoneNumber(signUpDto.getPhoneNumber()).isPresent();
        if (phoneNumberExists) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Phone number already exists");
        }

        boolean emailExists = userRepository.findByEmail(signUpDto.getEmail()).isPresent();
        if (emailExists) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        User user = new User(signUpDto.getName(), signUpDto.getEmail(), signUpDto.getPhoneNumber());

        userRepository.save(user);

        return createJwt(user);
    }

    private JwtDto createJwt(User user) {

        String accessToken = jwtIssuer.createAccessToken(user.getId(), user.getName(), user.getRole().toString());
        String refreshToken = jwtIssuer.createRefreshToken(user.getId(), user.getName(), user.getRole().toString());

        redisRefreshTokenService.saveRefreshToken(user.getId(), refreshToken);

        return JwtDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .grantType("Bearer")
                .build();
    }


}
