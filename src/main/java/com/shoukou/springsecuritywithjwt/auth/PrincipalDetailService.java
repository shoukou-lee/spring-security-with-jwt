package com.shoukou.springsecuritywithjwt.auth;

import com.shoukou.springsecuritywithjwt.user.User;
import com.shoukou.springsecuritywithjwt.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * /login 요청이 오면 자동으로 UserDatilsService 타입의 loadUserByUsername() 메서드가 실행됨
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PrincipalDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("username: {} ", username);

        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("UserNameNotFound"));

        // Session(Authentication(UserDetail(user))) 구조 중에서, UserDetail(user) 를 리턴
        return new PrincipalDetails(user);
    }

}
