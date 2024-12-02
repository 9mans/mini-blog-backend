package com.example.miniblog.service;

import com.example.miniblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
// 스프링 시큐리티에서 사용자 정보를 가져오기 위해 UserDetailsService 인터페이스를 구현한다
public class CustomUserDetailService implements UserDetailsService {


    @Autowired
    // 사용자 인증 정보를 데이터베이스에서 조회하는 용도
    private UserRepository userRepository;

    @Lazy
    @Autowired
    // 비밀번호를 암호화 하거나 암호화된 비밀번호를 검증하는데 사용
    private PasswordEncoder passwordEncoder;

    // 사용자 인증 로직을 정의한다
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 사용자 이름으로 데이터베이스에서 사용자 정보를 조회한다
        com.example.miniblog.entity.User user = userRepository.findByUsername(username)
                // 사용자 정보를 찾을 수 없을 경우 orElseThrow 메서드가 실행된다
                .orElseThrow(() -> new UsernameNotFoundException("User not found:" + username));

        // 시큐리티의 User 객체를 생성한다 사용자의 이름을 설정한다
        return User.withUsername(user.getUsername())
                // 데이터베이스에서 조회한 암호화된 비밀번호를 설정한다
                .password(user.getPassword())
                // 사용자의 역할을 설정한다 USER의 역할을 부여한다
                .roles("USER")
                .build();
    }

    // 새로운 사용자를 데이터베이스에 저장하는 로직을 정의한다
    public com.example.miniblog.entity.User save(com.example.miniblog.entity.User user) {
        // 사용자가 입력한 비밀번호를 PasswordEncoder를 사용해 암호화한다
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
