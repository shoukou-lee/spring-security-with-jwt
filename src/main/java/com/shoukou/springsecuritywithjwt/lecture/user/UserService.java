package com.shoukou.springsecuritywithjwt.lecture.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    @Transactional
    public UserDto join(UserDto userDto) {

        User user = new User(userDto.getUsername(), userDto.getPassword(), userDto.getEmail());
        user.setRole(UserRole.ROLE_USER);

        /**
         패스워드 encryption이 없으면 시큐리티 로그인이 불가능
         WARN 8585 ---BCryptPasswordEncoder: Encoded password does not look like BCrypt
         */
        String rawPassword = userDto.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);

        userRepository.save(user);

        return userDto;
    }

}
