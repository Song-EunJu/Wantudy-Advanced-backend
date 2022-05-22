package com.example.wantudy.oauth;

import lombok.Getter;

@Getter
public class AuthorizationGoogle {
    private String access_token;
    private String expires_in;
    private String token_type;
    private String scope;
    private String refresh_token;
}
