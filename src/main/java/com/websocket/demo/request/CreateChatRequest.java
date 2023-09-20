package com.websocket.demo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateChatRequest {

    private String sender;
    @NotBlank
    private String message;
    private Long roomId;
}
