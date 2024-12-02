package com.example.miniblog.controller;

import com.example.miniblog.entity.User;
import com.example.miniblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// RESTful 컨트롤러임을 나타냄
// 메서드가 반환하는 객체는 자동으로 JSON 형식으로 직렬화
@RestController
// 이 컨트롤러가 처리하는 메서드는 모든 요청이 아래 경로로 시작됨
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    // ResponseEntity는 HTTP 응답을 표현하는 객체
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    // {} URL 경로 변수
    @GetMapping("/{username}")
    // @PathVariable URL에서 {} 변수 값을 가져와 매개 변수에 바인딩
    public ResponseEntity<User> getUser(@PathVariable String username) {
        return userService.findByUsername(username)
                // 사용자가 존재하면 HTTP 200 응답과 함께 해당 사용자를 반환
                .map(ResponseEntity::ok)
                // 사용자가 존재하지 않으면 404 응답 반환
                .orElse(ResponseEntity.notFound().build());
    }
}
