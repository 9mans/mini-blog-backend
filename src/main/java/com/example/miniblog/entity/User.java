package com.example.miniblog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user")
// 유저 엔티티 클래스 생성
public class User {

    // 기본키로 지정
    @Id
    // 기본키를 생성 방법 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true, name = "username")
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

}
