package com.example.wantudy.global.security.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * spring security에서 사용자 정보를 담는 인터페이스 : UserDetails 인터페이스
 * 이 인터페이스를 구현하게 되면 spring security에서 구현한 클래스를 사용자 정보로 인식하고 인증 작업함
 */
@Getter
@Setter
public class CustomUserDetails implements OAuth2User, UserDetails {
    private final User user;
    private Map<String, Object> attributes;

    // UserDetails : Form 로그인 시 사용
    public CustomUserDetails(User user) {
        this.user = user;
    }

    // OAuth2User : OAuth2 로그인 시 사용
    public CustomUserDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    // 계정이 가진 권한 목록을 리턴한다
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() { // 계정이 만료되지 않았는지
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { // 계정이 잠기지 않았는지
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() { // 비밀번호 만료되지 않았는지
        return true;
    }

    @Override
    public boolean isEnabled() { // 계정이 활성화인지
        return true;
    }

    public Long getUserId(){
        return user.getUserId();
    }

    @Override
    public String getName() {
        String sub = attributes.get("sub").toString();
        return sub;
    }
}
