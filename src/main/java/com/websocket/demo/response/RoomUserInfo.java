package com.websocket.demo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.websocket.demo.domain.RoomUserData;
import lombok.Data;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@Data
public class RoomUserInfo {
    private Long roomId;
    private String nickname;
    @JsonInclude(Include.NON_NULL)
    private LocalDateTime time;

    public static RoomUserInfo from(RoomUserData data) {
        var result = new RoomUserInfo();
        result.setTime(data.getCheckTime());
        result.setRoomId(data.getRoom().getId());
        result.setNickname(data.getUserNickname());
        return result;
    }
}
