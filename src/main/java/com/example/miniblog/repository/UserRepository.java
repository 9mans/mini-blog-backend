package com.example.miniblog.repository;

import com.example.miniblog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// JPA 기본 CRUD 및 페이징 기능을 지원하는 인터페이스를 상속함 제네릭 타입은 <엔티티 클래스, 기본키의 타입>
public interface UserRepository extends JpaRepository<User, Long> {

    // 조회 결과가 빈값일 때 안전한 처리를 위해 optional 사용 쿼리메서드를 이용하여 조회
    Optional<User> findByUserName(String username);
}
