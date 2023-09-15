package com.websocket.demo.response;

import com.websocket.demo.domain.Chat;
import lombok.Data;

@Data
public class ChatInfo {
    private String sender;
    private String message;
    private Long roomId;

    public static ChatInfo from(Chat chat){
        var chatInfo = new ChatInfo();
        chatInfo.setMessage(chat.getMessage());
        chatInfo.setSender(chat.getSenderNickname());
        chatInfo.setRoomId(chat.getRoomId());
        return chatInfo;
    }
}