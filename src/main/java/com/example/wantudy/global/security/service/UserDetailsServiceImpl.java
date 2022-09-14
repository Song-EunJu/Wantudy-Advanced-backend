package com.example.wantudy.global.security.service;

import com.example.wantudy.global.security.dto.AuthResponse;
import com.example.wantudy.global.security.provider.TokenProvider;
import com.example.wantudy.global.security.domain.CustomUserDetails;
import com.example.wantudy.global.security.repository.UserRepository;
import com.example.wantudy.global.security.domain.User;
import com.example.wantudy.global.security.dto.AuthorizationGoogle;
import com.example.wantudy.global.security.dto.AuthorizationKakao;
import com.example.wantudy.global.security.model.ProviderType;
import com.example.wantudy.global.security.dto.GoogleUserInfo;
import com.example.wantudy.global.security.dto.KakaoUserInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
// spring security 에서 유저 인증, 인가 처리를 할 수 있게 유저 정보를 조회하는 서비스
/**
 * UserDetailsService 인터페이스에서, DB로부터 유저정보를 불러오는 메소드 : loadUserByUsername()
 * */
public class UserDetailsServiceImpl implements UserDetailsService {
    private final OAuth2Kakao oauth2Kakao;
    private final OAuth2Google oAuth2Google;
    private final UserRepository userRepository;
    private final AuthenticationManager authManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenProvider tokenProvider;

    public UserDetailsServiceImpl(OAuth2Kakao oauth2Kakao, OAuth2Google oAuth2Google, UserRepository userRepository, @Lazy AuthenticationManager authManager, @Lazy BCryptPasswordEncoder bCryptPasswordEncoder, TokenProvider tokenProvider) {
        this.oauth2Kakao = oauth2Kakao;
        this.oAuth2Google = oAuth2Google;
        this.userRepository = userRepository;
        this.authManager=authManager;
        this.bCryptPasswordEncoder=bCryptPasswordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email); //없으면 null, 있으면 user 객체 return
    }


    // loadUserByUsername 은 DB에 접근해서 사용자 정보를 가져오는 역할을 함
    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email);
        return new CustomUserDetails(user);
    }

    public void saveUser(User user){
        userRepository.save(user);
    }

    public AuthResponse oauth2AuthorizationKakao(String code) throws JsonProcessingException {
        // 카카오에서 토큰 받아오기
        AuthorizationKakao authorization = oauth2Kakao.callTokenApi(code);
        System.out.println("authorization = " + authorization.getAccess_token());
        KakaoUserInfo kakaoUserInfo = oauth2Kakao.callGetUserByAccessToken(authorization.getAccess_token());
        System.out.println("kakaoUserInfo = " + kakaoUserInfo.getAttributes());

        String email = kakaoUserInfo.getEmail();
        String profileImage = kakaoUserInfo.getProfileImage();
        String password = kakaoUserInfo.getProviderId() + email;
        String nickname = kakaoUserInfo.getName();
        User user = findByEmail(email);

        if (user == null) {
            String encodedPassword = bCryptPasswordEncoder.encode(password);
            user = new User(email, encodedPassword, profileImage, nickname);
            user.setProviderType(ProviderType.KAKAO);
            userRepository.save(user);
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication auth = authManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwtToken = tokenProvider.generateToken(auth);
        System.out.println("jwtToken = " + jwtToken);
        AuthResponse authResponse = new AuthResponse(jwtToken, user.getUserId());
        return authResponse;
    }

    public AuthResponse oauth2AuthorizationGoogle(String code) throws JsonProcessingException {
        AuthorizationGoogle authorization = oAuth2Google.callTokenApi(code);
        System.out.println("authorization = " + authorization.getAccess_token());
        GoogleUserInfo googleUserInfo = oAuth2Google.callGetUserByAccessToken(authorization.getAccess_token());
        System.out.println("googleUserInfo = " + googleUserInfo.getAttributes());

        String email = googleUserInfo.getEmail();
        String profileImage = googleUserInfo.getProfileImage();
        String password = googleUserInfo.getProviderId() + email;
        String nickname = googleUserInfo.getName();
        User user = findByEmail(email);

        if (user == null) {
            String encodedPassword = bCryptPasswordEncoder.encode(password);
            user = new User(email, encodedPassword, profileImage, nickname);
            user.setProviderType(ProviderType.GOOGLE);
            userRepository.save(user);
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication auth = authManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwtToken = tokenProvider.generateToken(auth);
        System.out.println("jwtToken = " + jwtToken);
        AuthResponse authResponse = new AuthResponse(jwtToken, user.getUserId());
        return authResponse;
    }


//
//    public User saveUser(KakaoUserInfo kakaoUserInfo){
//        String email = kakaoUserInfo.getEmail();
//        String profileImage = kakaoUserInfo.getProfileImage();
//        String password = kakaoUserInfo.getProviderId() + email;
//        String nickname = kakaoUserInfo.getName();
//        User user = findByEmail(email);
//
//        if(user == null){
//            String encodedPassword = bCryptPasswordEncoder.encode(password);
//            user = new User(email, encodedPassword, profileImage, nickname, ProviderType.KAKAO);
//            userRepository.save(user);
//        }
//        return user;
//    }

//    public AuthResponse saveUserInfoInSecurity(String email, String password){
//        User user = findByEmail(email);
//        System.out.println("user 2= " + user);
//        UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(email,password);
//        Authentication auth=authManager.authenticate(authToken);
//        SecurityContextHolder.getContext().setAuthentication(auth);
//        System.out.println("auth = " + auth);
//        String jwtToken = tokenProvider.generateJwtToken(auth);
//        System.out.println("jwtToken = " + jwtToken);
//        AuthResponse authResponse = new AuthResponse(jwtToken, user.getUserId());
//        return authResponse;
//    }
}
