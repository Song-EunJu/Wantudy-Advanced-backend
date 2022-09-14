package com.example.wantudy.global.config;
import com.example.wantudy.global.security.filter.TokenAuthenticationFilter;
import com.example.wantudy.global.security.provider.TokenProvider;
import com.example.wantudy.global.security.service.CustomOAuth2UserService;
import com.example.wantudy.global.security.handler.CustomSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final TokenAuthenticationFilter tokenAuthenticationFilter;
    private final CustomSuccessHandler customSuccessHandler;
    private final TokenProvider tokenProvider;

    public WebSecurityConfig(@Lazy CustomOAuth2UserService customOAuth2UserService, TokenAuthenticationFilter tokenAuthenticationFilter, CustomSuccessHandler customSuccessHandler, TokenProvider tokenProvider) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.tokenAuthenticationFilter = tokenAuthenticationFilter;
        this.customSuccessHandler = customSuccessHandler;
        this.tokenProvider = tokenProvider;
    }

    //AuthenticationManagerBean 등록 -> 하단의 configure에서 LoginForm을 이용한 자동 처리 사용 X, 수동으로 Authentication을 만들어서 SecurityContext에 저장
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean()  throws Exception{
        return super.authenticationManagerBean();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        SimpleUrlAuthenticationSuccessHandler handler = new CustomSuccessHandler(tokenProvider);
        handler.setUseReferer(true);
        return handler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .cors().and()
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .userInfoEndpoint()// 로그인 성공 후 사용자 정보를 가져옴
                .userService(customOAuth2UserService)// userInfoEndpoint()로 가져온 사용자 정보를 처리할 때 사용
                .and()
                .successHandler(successHandler())
////                .defaultSuccessUrl("/nickname",true) // GetMapping의 /nickname으로 가서 깃허브 유저네임 중복 체크!
                .and()
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
