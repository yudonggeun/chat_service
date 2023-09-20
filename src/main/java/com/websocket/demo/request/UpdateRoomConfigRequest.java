package com.websocket.demo.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateRoomConfigRequest {
    private Long roomId;
    private String backgroundColor;
}
