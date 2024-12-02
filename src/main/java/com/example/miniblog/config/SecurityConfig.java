package com.example.miniblog.config;

import com.example.miniblog.filter.JwtFilter;
import com.example.miniblog.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// 이 클래스는 스프링의 설정 클래스임을 나타낸다
// 설정 관련 빈을 정의하고 spring 컨텍스트에 등록한다
@Configuration
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public SecurityConfig(UserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }
    // 이 메서드는 스프링 컨텍스트에서 관리하는 빈으로 등록된다
    @Bean
    // 애플리케이션 전역에서 비밀번호를 암호화하거나 검증하는데 사용한다
    public PasswordEncoder passwordEncoder() {
        // 암호화를 위한 BCrypt 알고리즘을 사용한다
        return new BCryptPasswordEncoder();
    }

    @Bean
    // 시큐리티의 인증 관리자읜 AuthenticationManager를 등록한다
    // AuthenticationConfiguration 시큐리티가 제공하는 클래스 인증설정에 필요한 구성 정보를 포함한다
    // AuthenticationManager는 사용자 인증을 처리하는 핵심 컴포넌트로 사용자 이름과 비밀번호를 확인하거나 토큰 기반 인증을 수행한다
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
        http.csrf(csrf -> csrf.disable()) // CSRF 비활성화
            .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/**").permitAll() // 인증 없이 허용
            .anyRequest().authenticated()) // 나머지 요청은 인증 필요
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // JWT 필터 추가

        return http.build();
    }

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(jwtUtil, userDetailsService);
    }
}
