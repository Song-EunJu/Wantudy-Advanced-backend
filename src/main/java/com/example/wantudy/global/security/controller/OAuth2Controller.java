package com.example.wantudy.global.security.controller;

import com.example.wantudy.global.security.dto.AuthResponse;
import com.example.wantudy.global.common.ResponseMessage;
import com.example.wantudy.global.security.service.UserDetailsServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class OAuth2Controller {
    private final UserDetailsServiceImpl userDetailsService;

    @GetMapping("/")
    public String index(){
        return "main";
    }


    @GetMapping("/oauth/callback/kakao")
    public ResponseEntity<ResponseMessage> oauth2AuthorizationKakao(@RequestParam("code") String code) throws JsonProcessingException {
        AuthResponse authResponse = userDetailsService.oauth2AuthorizationKakao(code);
        return new ResponseEntity<>(ResponseMessage.withData(200, "로그인 성공", authResponse), HttpStatus.OK);
    }
//    http://localhost:8080/login/oauth2/code/google
    @GetMapping("/login/oauth2/code/google")
    public ResponseEntity<ResponseMessage> oauth2AuthorizationGoogle(@RequestParam("code") String code) throws JsonProcessingException {
        AuthResponse authResponse = userDetailsService.oauth2AuthorizationGoogle(code);
        return new ResponseEntity<>(ResponseMessage.withData(200, "로그인 성공", authResponse), HttpStatus.OK);
    }
}
