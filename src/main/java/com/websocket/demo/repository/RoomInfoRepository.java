package com.websocket.demo.repository;

import com.websocket.demo.domain.RoomUserData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomInfoRepository extends JpaRepository<RoomUserData, Long> {
    void deleteByUserNicknameAndRoomId(String nickname, Long roomId);

    boolean existsByUserNicknameAndRoomId(String nickname, Long roomId);
}
