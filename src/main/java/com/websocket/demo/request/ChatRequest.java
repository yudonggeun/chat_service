package com.websocket.demo.request;

import lombok.Data;

@Data
public class ChatRequest {

    private String sender;
    private String message;
}
