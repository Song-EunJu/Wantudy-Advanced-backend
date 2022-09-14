package com.example.wantudy.global.security.handler;

import com.example.wantudy.global.security.provider.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 로그인 성공 시 처리할 로직
 * 차이점이 뭐죠? SimpleUrlAuthenticationSuccessHandler vs authenticationsuccesshandler
 * */
@RequiredArgsConstructor
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;

    private String redirectUri="test";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        handle(request, response, authentication);
        super.clearAuthenticationAttributes(request);
    }

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        System.out.println("handler called");
        String targetUrl = determineTargetUrl(request, response, authentication);

        getRedirectStrategy().sendRedirect(request, response, checkNickname(authentication,request,targetUrl));
    }


    private String checkNickname(Authentication authentication,HttpServletRequest request,String targetUrl){
        System.out.println("닉네임 고치는부분");
        return "test";
//        User user=(User)request.getSession().getAttribute("user");
//        Long savedUserId=user.getUserId();
//        System.out.println("userID: "+savedUserId);
////        String nodeId=(String)request.getSession().getAttribute("nodeId");
//
//        String userNickname=user.getUserNickname();
//
//        if(userNickname.contains("_CONFLICT")){
//            //중복 닉네임이 있는 유저라는 의미이므로, 새로운 닉네임 폼으로 보내주어야 한다
//            return UriComponentsBuilder.fromUriString(targetUrl).path("nickname/{userId}")
//                    .build().expand(savedUserId).toUriString();
//        }else{
//            String token = tokenProvider.generateJwtToken(authentication);
//            String res=UriComponentsBuilder.fromUriString(targetUrl).path("github-login/{userId}/{token}")
//                    .buildAndExpand(savedUserId,token).toUriString();
//            System.out.println(res);
////            res=targetUrl+"github-login/"+savedUserId;
//            return res;
//
//        }
    }
}