package com.websocket.demo.repository;

import com.websocket.demo.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByDataUserNickname(String nickname);
}
