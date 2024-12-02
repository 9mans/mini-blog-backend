package com.example.miniblog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    // 엔티티간의 N:1 관계를 정의함
    // 이 클래스(Post)가 해당 필드의 타입과 N:1관계라는 것을 의미한다
    @ManyToOne
    // Post 테이블에 author_id라는 외래 키 컬럼이 생성 이 컬럼은 User의 기본 키를 참조한다.
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    private LocalDateTime createDate = LocalDateTime.now();

    private LocalDateTime updateDate = LocalDateTime.now();
}
