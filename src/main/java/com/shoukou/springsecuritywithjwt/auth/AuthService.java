package com.shoukou.springsecuritywithjwt.auth;

import com.shoukou.springsecuritywithjwt.jwt.JwtDto;
import com.shoukou.springsecuritywithjwt.jwt.JwtIssuer;
import com.shoukou.springsecuritywithjwt.user.LoginDto;
import com.shoukou.springsecuritywithjwt.user.User;
import com.shoukou.springsecuritywithjwt.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final JwtIssuer jwtIssuer;

    public JwtDto login(LoginDto loginDto) {

        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "user not found"));

        return createJwt(user);
    }

    private JwtDto createJwt(User user) {

        String accessToken = jwtIssuer.createAccessToken(user.getName(), user.getRole().toString());
        String refreshToken = jwtIssuer.createRefreshToken(user.getName(), user.getRole().toString());

        return JwtDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .grantType("Bearer")
                .build();

    }

}
