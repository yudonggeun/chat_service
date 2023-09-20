package com.websocket.demo.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CheckRoomRequest {
    private Long roomId;
    private LocalDateTime checkTime = LocalDateTime.now();
}
