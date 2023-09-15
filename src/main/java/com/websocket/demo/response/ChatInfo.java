package com.websocket.demo.response;

import com.websocket.demo.domain.Chat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatInfo {
    private Long id;
    private String sender;
    private String message;
    private LocalDateTime createdAt;
    private Long roomId;

    public static ChatInfo from(Chat chat){
        var chatInfo = new ChatInfo();
        chatInfo.setId(chat.getId());
        chatInfo.setMessage(chat.getMessage());
        chatInfo.setSender(chat.getSenderNickname());
        chatInfo.setRoomId(chat.getRoomId());
        chatInfo.setCreatedAt(chat.getCreatedAt());
        return chatInfo;
    }
}