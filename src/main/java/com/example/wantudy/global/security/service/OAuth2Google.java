package com.example.wantudy.global.security.service;

import com.example.wantudy.global.security.dto.AuthorizationGoogle;
import com.example.wantudy.global.security.dto.GoogleUserInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@RequiredArgsConstructor
@Service
@Configuration
//@PropertySource(value = "application.properties")
public class OAuth2Google {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    public AuthorizationGoogle callTokenApi(String code) throws JsonProcessingException {
        String grantType = "authorization_code";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", grantType);
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);
        params.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        String url = "oauth2.googleapis.com";
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        AuthorizationGoogle authorization = objectMapper.readValue(response.getBody(), AuthorizationGoogle.class);
        return authorization;
    }

    /**
     * accessToken 을 이용한 유저 정보 받기
     */
    public GoogleUserInfo callGetUserByAccessToken(String accessToken) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        String url = "https://www.googleapis.com/oauth2/v2/userinfo/?access_token="+accessToken;

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        Map<String, Object> attributes = objectMapper.readValue(response.getBody(), Map.class);
        GoogleUserInfo googleUserInfo = new GoogleUserInfo(attributes);
        return googleUserInfo;
    }
}
