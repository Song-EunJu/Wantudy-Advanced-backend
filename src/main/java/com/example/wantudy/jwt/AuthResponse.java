package com.example.wantudy.jwt;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String jwtToken;
    Long userId;

    public AuthResponse(String jwtToken, Long id){
        this.jwtToken=jwtToken;
        this.userId=id;
    }
}
