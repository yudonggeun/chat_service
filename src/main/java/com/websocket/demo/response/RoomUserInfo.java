package com.websocket.demo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@Data
public class RoomUserInfo {
    private Long roomId;
    private String nickname;
    @JsonInclude(Include.NON_NULL)
    private LocalDateTime time;
}
