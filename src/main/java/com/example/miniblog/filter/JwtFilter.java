package com.example.miniblog.filter;

import com.example.miniblog.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
// OncePerRequestFilter 스프링에서 제공하는 기본 필터 클래스로 HTTP 요청마다 단 한 번 실행된다
// 중복 요청 처리를 방지한다
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    // 시큐리티의 필터 체인에서 실행될 메서드로 요청과 응답을 처리함
    // 요청마다 실행되어 JWT를 검사하고 인증 정보를 설정한 후 요청을 다음 필터로 전달함
    // HttpServletRequest 클라이언트의 요청 정보
    // HttpServletResponse 서버가 응답할 데이터
    // FilterChain 현재 필터 이후에 실행될 필터 체인을 나타낸다
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // HTTP 요청 헤더에서 Authorization 값을 가져온다
        // JWT는 일반적으로 Bearer <토큰> 형식으로 전달된다
        String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String token = null;

        // authorizationHeader.startsWith("Bearer") Authorization 헤더가 Bearer로 시작하는지 확인한다
        // JWT가 포함된 요청인지 확인하는 단계
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
            // Bearer를 제거하고 순수한 JWT 토큰 문자열만 추출한다
            token = authorizationHeader.substring(7);
            // jwtUtil을 이용해 JWT에서 사용자 이름을 추출한다
            username = jwtUtil.extractUsername(token);
        }

        // 인증되지 않은 사용자 처리
        // JWT에서 사용자 이름을 성공적으로 추출하거나 && 현재 요청이 이미 인증되지 않은 경우에만 처리
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 데이터베이스 또는 기타 저장소에서 사용자 정보를 로드 반환 값은 UserDetails 객체
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 토큰이 유효한지, 사용자 이름이 일치하는지 확인
            if (jwtUtil.validateToken(token, userDetails.getUsername())) {
                // 시큐리티에서 인증 객체를 생성한다
                UsernamePasswordAuthenticationToken authenticationToken =
                        // userDetails.getAuthorities()로 사용자 권한도 설정합니다.
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                // 인증 객체를 시큐리티의 컨텍스트에 설정한다
                // 이후 요청은 인증된 상태로 처리된다
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }
        }

        // 다음 필터 호출
        // 필터 체인의 다음 필터로 요청과 응답을 전달한다
        // 모든 필터를 실행 후 최종적으로 컨트롤러 또는 서블릿으로 요청이 전달된다
        filterChain.doFilter(request, response);
    }
}
