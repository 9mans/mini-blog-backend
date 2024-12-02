package com.example.miniblog.service;

import com.example.miniblog.entity.User;
import com.example.miniblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    // Spring Bean 자동 생성
    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        // JPA가 제공하는 기본 메서드 save = 새 엔티티를 저장하거나, 기존 엔티티를 업데이트함
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
