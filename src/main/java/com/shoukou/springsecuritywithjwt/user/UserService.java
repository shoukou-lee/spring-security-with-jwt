package com.shoukou.springsecuritywithjwt.user;

import com.shoukou.springsecuritywithjwt.security.jwt.JwtPrivateClaimExtractor;
import com.shoukou.springsecuritywithjwt.user.dto.UserDto;
import com.shoukou.springsecuritywithjwt.user.dto.UserListDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserListDto getUserList() {

        List<User> all = userRepository.findAllUsers();

        List<UserDto> userDtos = all.stream()
                .map(UserDto::new)
                .collect(Collectors.toList());

        return UserListDto.builder()
                .userDtos(userDtos)
                .build();

    }

    public UserDto getMyInfo() {

        Long myId = JwtPrivateClaimExtractor.getUserId();

        User user = userRepository.findById(myId)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "No user found"));

        return new UserDto(user);

    }

    @Transactional
    public UserDto ban(Long targetUserId) {

        Long requesterId = JwtPrivateClaimExtractor.getUserId();

        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "No user found"));

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "No user found"));

        // 자신보다 낮은 ROLE을 가진 사용자만 Ban 가능
        if (requester.getRole().compareTo(targetUser.getRole()) == 1) {
            targetUser.softDelete();
        } else {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "No permission to ban user");
        }

        return new UserDto(targetUser);
    }

}
