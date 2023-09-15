package com.websocket.demo.request;

import lombok.Data;

@Data
public class DeleteChatRequest {
    private Long roomId;
    private Long id;
}
