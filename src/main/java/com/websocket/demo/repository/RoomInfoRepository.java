package com.websocket.demo.repository;

import com.websocket.demo.domain.RoomUserData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomInfoRepository extends JpaRepository<RoomUserData, Long> {
    void deleteByUserNicknameAndRoomId(String nickname, Long roomId);

    boolean existsByUserNicknameAndRoomId(String nickname, Long roomId);

    Optional<RoomUserData> findByUserNicknameAndRoomId(String nickname, Long roomId);
}
