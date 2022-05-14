package com.shoukou.springsecuritywithjwt.user;

import com.shoukou.springsecuritywithjwt.security.WithCustomPrincipal;
import com.shoukou.springsecuritywithjwt.security.jwt.JwtAuthenticationToken;
import com.shoukou.springsecuritywithjwt.user.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles(profiles = {"test", "secret"})
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private final Long mockUserId = 1L;
    private final String mockUsername = "MockUser";

    @WithCustomPrincipal(userId = 1, username = "test")
    @Test
    void securityContextHolderTest() {

        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        assertThat(authentication.getPrincipal().getUserId()).isEqualTo(1L);
        assertThat(authentication.getPrincipal().getUsername()).isEqualTo("test");

        String authority = authentication.getAuthorities().stream().findFirst().get().getAuthority();
        assertThat(authority).isEqualTo("ROLE_USER");
    }

    @WithCustomPrincipal
    @Test
    void getMyInfo() {
        User user = new User(mockUsername, "email", "phone");
        userRepository.save(user);

        UserDto ret = userService.getMyInfo();

        assertThat(ret.getId()).isEqualTo(mockUserId);
        assertThat(ret.getName()).isEqualTo(mockUsername);
        assertThat(ret.getEmail()).isEqualTo("email");
        assertThat(ret.getPhoneNumber()).isEqualTo("phone");

    }

    @Test
    void getMyInfoWithoutPrincipal() {
        User user = new User(mockUsername, "email", "phone");
        userRepository.save(user);

        assertThatThrownBy(() -> userService.getMyInfo())
                .isInstanceOf(NullPointerException.class);
    }

}
