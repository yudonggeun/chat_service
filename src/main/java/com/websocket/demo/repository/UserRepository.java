package com.websocket.demo.repository;

import com.websocket.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByNickname(String nickname);

    boolean existsByNickname(String nickname);
}
