package com.shoukou.springsecuritywithjwt.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    public UserDto getMyDetail() {
        //TODO
        // 토큰으로부터 내 정보를 파싱 후 DTO로 리턴하기
        return null;
    }

    public UserDto ban(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("No user found"))
                .softDelete();

        return new UserDto(user);

    }

}
