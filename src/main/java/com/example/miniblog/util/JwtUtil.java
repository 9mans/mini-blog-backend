package com.example.miniblog.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

// 이 클래스가 스프링 컨텍스트에서 관리되는 빈임을 나타낸다
@Component
public class JwtUtil {

    // JWT를 서명하거나 검증할 때 사용하는 비밀 키
    // 클라이언트와 공유되지 않는다
    private final String SECRET_KEY = "my_secret_key";

    //토큰 생성 메서드
    // 사용자 이름을 기반으로 JWT를 생성한다
    public String generateToken(String username) {
        return Jwts.builder()
                // JWT의 생성 주체 userName
                .setSubject(username)
                // 토큰 생성 시점
                .setIssuedAt(new Date())
                // 토큰 만료시간 설정 10시간 후
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                // SECRET_KEY를 사용하요 ES256 알고리즘으로 서명한다
                .signWith(SignatureAlgorithm.ES256, SECRET_KEY)
                // 최종적으로 JWT 문자열을 반환한다
                .compact();
    }

    // 사용자 이름 추출 메서드
    public String extractUsername(String token) {
        // JWT를 파싱하기 위한 파서 생성
        return Jwts.parser()
                // JWT를 검증할 때 사용할 서명 키
                .setSigningKey(SECRET_KEY)
                // JWT를 파싱하고 검증한다 유효하지 않은 토큰은 예외가 발생
                .parseClaimsJws(token)
                // JWT 본문(Claims)에서 subject 필드를 가져온다 이는 토큰 생성 시 설정한 사용자 이름이다
                .getBody()
                .getSubject();
    }

    // 토큰 유효성 검증 메서드
    public boolean validateToken(String token, String username) {
        // 토큰에서 사용자 이름을 추출
        // .equals 토큰에 저장된 이름과 주어진 사용자 이름이 일치하는지 확인
        // !isTokenExpired 토큰이 만료되지 않았는지 확인
        return username.equals(extractUsername(token)) && !isTokenExpired(token);
    }

    // 토큰 만료 확인 메서드
    private boolean isTokenExpired(String token) {
        // JWT 파서 생성 및 서명 키 설정
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                // JWT 본문에서 만료 시간을 가져온다
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                // 현재 시간과 만료 시간을 비교하여 만료 시간이 현재 시간보다 이전이라면 true 반환
                .before(new Date());
    }


}
