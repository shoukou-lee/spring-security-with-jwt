package com.shoukou.springsecuritywithjwt.user;

import com.shoukou.springsecuritywithjwt.security.jwt.JwtPrincipalExtractor;
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

        Long myId = JwtPrincipalExtractor.getUserId();
        User user = userRepository.findById(myId)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "No user found"));

        return new UserDto(user);
    }

    @Transactional
    public UserDto ban(Long targetUserId) {

        Long requesterId = JwtPrincipalExtractor.getUserId();

        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "No user found"));

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "No user found"));

        // requester.role > targetUser.role 일때만 허용
        if (requester.getRole().compareTo(targetUser.getRole()) == 1) {
            targetUser.softDelete();
        } else {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "BAN 권한 없음 !");
        }

        return new UserDto(targetUser);
    }

}
