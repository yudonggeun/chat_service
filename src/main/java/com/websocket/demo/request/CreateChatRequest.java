package com.websocket.demo.request;

import lombok.Data;

@Data
public class CreateChatRequest {

    private String sender;
    private String message;
    private Long roomId;
}
