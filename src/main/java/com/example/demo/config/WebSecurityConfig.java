package com.example.demo.config;

import com.example.demo.security.JwtAuthenticationFilter;
import com.example.demo.security.OAuthSuccessHandler;
import com.example.demo.security.OAuthUserServiceImpl;
import com.example.demo.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

//@Configuration

//@RequiredArgsConstructor
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class WebSecurityConfig {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuthUserServiceImpl oAuthUserService; // 만든 OAuthUserServiceImpl추가
    private final OAuthSuccessHandler oAuthSuccessHandler;


//    @Override
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // CORS 설정 적용
                .cors(Customizer.withDefaults())

                // CSRF 비활성화
                .csrf(csrf -> csrf.disable())

                // HTTP 기본 인증 비활성화
                .httpBasic(httpBasic -> httpBasic.disable())

                // 세션 사용하지 않음 (JWT 기반 인증)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 권한 및 요청 설정
                .authorizeHttpRequests(auth -> auth
                        // 인증 없이 허용되는 경로
                        .requestMatchers("/", "/auth/**","/oauth2/**").permitAll()

                        // 그 외 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )

                .oauth2Login(oauth2 -> oauth2
                        .redirectionEndpoint(redirection ->
                                redirection.baseUri("/oauth2/login/code/*"))
                        .userInfoEndpoint(user->user.userService(oAuthUserService))
                        .successHandler(oAuthSuccessHandler)
                )

                // JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 등록
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // 최종 필터 체인 구성
                .build();
    }

    // AuthenticationManager 빈 등록 (로그인 인증 등에 사용)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}