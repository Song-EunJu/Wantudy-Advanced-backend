package com.example.wantudy.global.security.service;

import com.example.wantudy.global.security.domain.CustomUserDetails;
import com.example.wantudy.global.security.domain.User;
import com.example.wantudy.global.security.dto.OAuth2UserInfo;
import com.example.wantudy.global.security.model.ProviderType;
import com.example.wantudy.global.security.dto.GoogleUserInfo;
import com.example.wantudy.global.security.dto.KakaoUserInfo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

/**
 * - OAuth2 로그인이면 OAuth2UserService 의 loadUserByUsername 메소드 실행
 * - loadUserByUsername메서드는 "이런 정보가 들어왔는데 얘 혹시 회원이야?" 라고 묻는 메소드
 *   = loadUserByUsername에서는 회원을 찾아주는 로직을 구현
 * - 이 때 회원정보는 OAuth2User타입으로 반환
 * - UserDetails 또는 OAuth2User를 반환하면 Spring에서 알아서 Session에 저장해준다.
 *
 * OAuth2로 로그인하는 사용자는 회원가입을 거치지 않기 때문에 DB에 유저가 없다면 회원가입처리,
 *
 * 유저가 있다면 Authentication(OAuth2User를 구현한 CustomUserDetails)를 반환하여 SecurityContextHolder에 저장
 * - 로그인 후 처리해야 할 메소드
 * */
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserDetailsServiceImpl userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final HttpSession httpSession;

    public CustomOAuth2UserService(BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailsServiceImpl userDetailsService, HttpSession httpSession) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userDetailsService = userDetailsService;
        this.httpSession = httpSession;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        /** 유저의 정보를 가져옴 */
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo = null;

        /** 인증 서버 (구글) 의 정보를 가져옴 */
        String provider = userRequest.getClientRegistration().getRegistrationId();

        if(provider.equals("kakao"))
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        else if(provider.equals("google")){
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }
        System.out.println("oAuth2UserInfo = " + oAuth2UserInfo);

        String id = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        String password = bCryptPasswordEncoder.encode(id + email);
        String nickname = oAuth2UserInfo.getName();
        String profileImage = oAuth2UserInfo.getProfileImage();
        User existUser = userDetailsService.findByEmail(email);

        //DB에 없는 사용자라면 회원가입처리
        if(existUser == null){
            existUser = User.builder()
                    .email(email)
                    .password(password)
                    .nickname(nickname)
                    .profileImage(profileImage)
                    .build();

            switch (provider) {
                case "kakao":
                    existUser.setProviderType(ProviderType.KAKAO);
                    break;
                case "google":
                    existUser.setProviderType(ProviderType.GOOGLE);
                    break;
            }
            userDetailsService.saveUser(existUser);
        }

        CustomUserDetails customUserDetails = new CustomUserDetails(existUser, oAuth2User.getAttributes());
        httpSession.setAttribute("user",existUser); // /nickname에서 방금 로그인 한 사용자 정보 찾기 위해

        return customUserDetails;
    }
}
