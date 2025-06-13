package com.example.demo.config;

import com.example.demo.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    // JWT 인증 필터 (직접 구현한 클래스)
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Spring Security의 필터 체인을 구성하는 Bean.
     * 최신 방식으로, WebSecurityConfigurerAdapter 없이 설정 가능.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // CORS 설정: WebMvcConfig에서 설정했다면 기본값으로 활성화
                .cors(Customizer.withDefaults())

                // CSRF 보호 비활성화 (JWT 기반 REST API에서는 일반적으로 꺼둠)
                .csrf(csrf -> csrf.disable())

                // HTTP 기본 인증 사용하지 않음 (Authorization 헤더로만 처리할 예정)
                .httpBasic(httpBasic -> httpBasic.disable())

                // 세션 사용하지 않음 → STATELESS로 설정 (JWT 인증 방식이므로)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 요청별 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // 인증 없이 접근 허용할 경로
                        .requestMatchers("/", "/auth/**").permitAll()
                        // 나머지 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )

                // JWT 인증 필터 등록: UsernamePasswordAuthenticationFilter 앞에 위치해야 함
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // 필터 체인 객체로 build
                .build();
    }

    /**
     * AuthenticationManager를 수동으로 등록 (로그인 처리에 사용)
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}