package com.example.wantudy.global.security.provider;//package com.example.wantudy.domain.jwt;

import com.example.wantudy.global.security.domain.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@PropertySource("classpath:application.properties")
/**
 * TokenProvider : 유저 정보로 JWT 토큰을 만들거나, 토큰을 바탕으로 유저 정보를 가져옴
 * */
public class TokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일

    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_ISSUER = "Wantudy";
    private static final long tokenValidTime = 1000L * 60 * 60 * 10;

    @Value("${auth.jwtSecret}")
    private String secretKey;

    private final Key key;

    public TokenProvider(@Value("${auth.jwtSecret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // jwt 토큰 생성
    public String generateToken(Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        byte[] signingKey = secretKey.getBytes();

        /**
         * jwt 토큰의 payload 부분에는 토큰에 담을 정보가 있음
         * 정보의 한 '조각' = claim (json형태의 한 쌍으로 이뤄져있음)
         * */
        return Jwts.builder()
                .setHeaderParam("typ", TOKEN_TYPE)
                .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(tokenValidTime).toInstant()))
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setId(UUID.randomUUID().toString())
                .setIssuer(TOKEN_ISSUER)
                .setSubject(user.getUsername())
                .claim("rol", roles)
                .claim("email", user.getUsername())
                .claim("userId", user.getUserId())
                .compact();
    }

    /**
     * - JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼냄
     *
     * */
    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        /**
         * UserDetails 객체를 만들어서 Authentication 리턴
         * subject, issue, audience, id, expiration 은 getBody()를 사용하여 읽을 수 있음
         */
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    private Claims parseClaims(String accessToken) {
        try {
            /**
             * - parseClaimsJws(accessToken) 까지하면 Jws<Claims>
             * - JWS(JSON Web Signature) : 서버에서 인증을 증거로 인증 정보를 서버의 private key로 서명한 것을 토큰화한 것
             * */
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
}
//
//import com.example.wantudy.oauth.CustomUserDetails;
//import com.example.wantudy.oauth.domain.User;
//import com.example.wantudy.oauth.service.UserDetailsServiceImpl;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jws;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//
//import javax.servlet.http.HttpServletRequest;
//import java.nio.charset.StandardCharsets;
//import java.util.Date;
//import java.util.UUID;
//
//@Component
//public class TokenProvider {
//
//    @Value("${auth.jwtSecret}")
//    private String secretKey;
//
//    private long tokenValidTime = 1000L * 60 * 60*10; // 위와 마찬가지
//
//    private final UserDetailsServiceImpl userDetailsService;
//
//    public TokenProvider(@Lazy UserDetailsServiceImpl userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }
//
//    // JWT 토큰 생성
//    public String generateJwtToken(Authentication auth) {
//
//        //authencitaion에서 로그인한 user의 정보 가져오기
//        CustomUserDetails customUserDetails = (CustomUserDetails)auth.getPrincipal();
//        User user = customUserDetails.getUser();
//        Long userId=user.getUserId();
//        String userEmail=user.getEmail();
//        System.out.println("generate jwt token");
//
//        byte[] signKey=secretKey.getBytes(StandardCharsets.UTF_8);
//        //claims에는 사용자와 관련된 내용들 중 jwt에 포함할 내용을 지정해서 저장
//        //여기서는 email, role, userid
//
//        //이 인증된 user가 가지고 있는 authority를 list 형태로 get
//
//        Date now = new Date();
//
//        return Jwts.builder()
//                .setIssuedAt(now) // 토큰 발행 시간 정보
//                .setExpiration(new Date(now.getTime() + tokenValidTime)) // set Expire Time
//                .signWith(Keys.hmacShaKeyFor(signKey), SignatureAlgorithm.HS512)
//                .setId(UUID.randomUUID().toString())
//                .setIssuer("Wantudy")
//                .setHeaderParam("typ","JWT")
//                .claim("email",userEmail)
//                .claim("userId",userId)
//                .compact();
//    }
//
//    // 프론트에서 로그인 후 jwtToken받은 후, ['Authorization']="Bearer "+jwtToken 으로 axios header가 설정된 상태로 서버에 데이터 요청
//    public String getJwtTokenFromRequestHeader(HttpServletRequest request) {
//        String token;
//        String tokenHeader=request.getHeader("Authorization"); // axios.defaults.header.common['Authorization']='Bearer "+token -> front
//        if(StringUtils.hasText(tokenHeader)&&tokenHeader.startsWith("Bearer ")){
////            System.out.println("found token starts with bearer");
//            token=tokenHeader.replace("Bearer ","");
//            return token;
//        }
//
//        return null;
//    }
//
//    // 토큰의 유효성 + 만료일자 확인
//    public Jws<Claims> validateToken(String jwtToken) {
//        try {
////            System.out.println("jwtToken = " + jwtToken);
//            byte[] signKey=secretKey.getBytes(StandardCharsets.UTF_8);
//            return Jwts.parserBuilder()
//                    .setSigningKey(signKey)
//                    .build()
//                    .parseClaimsJws(jwtToken);
//        } catch (Exception e) {
//            System.out.println("Token Validation Request Failed");
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//}
