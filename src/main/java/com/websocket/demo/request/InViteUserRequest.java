package com.websocket.demo.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InViteUserRequest {
    private Long roomId;
    private String nickname;
}
