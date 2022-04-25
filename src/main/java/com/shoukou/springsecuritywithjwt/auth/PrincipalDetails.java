package com.shoukou.springsecuritywithjwt.auth;

import com.shoukou.springsecuritywithjwt.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * security가 /login 주소 요청이 오면 낚아채서 로그인을 대신 진행해줌
 * 로그인이 완료되면 security session을 만든다. (Security ContextHolder에 세션 정보가 저장됨)
 * Security ContextHolder에 들어갈 수 있는 타입은 Authentication 타입 객체여야 함
 * Authentication 객체 안에 User 정보는 UserDetails 타입의 객체를 사용
 *
 * UserDetail로 User 객체 정보를 찾아내기 위한 클래스
 */
@Getter
@Setter
@ToString
public class PrincipalDetails implements UserDetails {

    private User user;

    public PrincipalDetails(User user) {
        this.user = user;
    }

    // 해당 User의 권한을 리턴하는 메서드
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();

        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole().toString();
            }
        });

        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        // 가짜 구현
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 가짜 구현
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 가짜 구현
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 가짜 구현
        // 예를 들면 1년 동안 로그인을 안해서 휴면계정이 된 경우, false;
        return true;
    }
}
