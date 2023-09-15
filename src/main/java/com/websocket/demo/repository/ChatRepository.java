package com.websocket.demo.repository;

import com.websocket.demo.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByRoomIdAndCreatedAtBetween(Long roomId, LocalDateTime to, LocalDateTime from);
}
