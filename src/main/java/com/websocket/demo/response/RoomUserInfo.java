package com.websocket.demo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.websocket.demo.domain.RoomUserData;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@Data
public class RoomUserInfo {
    private Long roomId;
    private String nickname;
    @JsonInclude(Include.NON_NULL)
    private String backgroundColor;
    @JsonInclude(Include.NON_NULL)
    private LocalDateTime time;

    @Builder
    private RoomUserInfo(Long roomId, String nickname, String backgroundColor, LocalDateTime time) {
        this.roomId = roomId;
        this.nickname = nickname;
        this.backgroundColor = backgroundColor;
        this.time = time;
    }

    public static RoomUserInfo from(RoomUserData data) {
        return RoomUserInfo.builder()
                .time(data.getCheckTime())
                .roomId(data.getRoom().getId())
                .nickname(data.getUserNickname())
                .backgroundColor(data.getBackgroundColor())
                .build();
    }
}
