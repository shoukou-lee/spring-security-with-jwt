package com.shoukou.springsecuritywithjwt.auth;

import com.shoukou.springsecuritywithjwt.jwt.JwtDto;
import com.shoukou.springsecuritywithjwt.jwt.JwtIssuer;
import com.shoukou.springsecuritywithjwt.user.LoginDto;
import com.shoukou.springsecuritywithjwt.user.SignUpDto;
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

    @Transactional
    public JwtDto signUp(SignUpDto signUpDto) {

        boolean userNameExists = userRepository.findByUsername(signUpDto.getName()).isPresent();
        if (userNameExists) {
            throw new RuntimeException("User name already exists");
        }

        boolean phoneNumberExists = userRepository.findByPhoneNumber(signUpDto.getPhoneNumber()).isPresent();
        if (phoneNumberExists) {
            throw new RuntimeException("Phone number already exists");
        }

        boolean emailExists = userRepository.findByEmail(signUpDto.getEmail()).isPresent();
        if (emailExists) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User(signUpDto.getName(), signUpDto.getEmail(), signUpDto.getPhoneNumber());

        userRepository.save(user);

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
