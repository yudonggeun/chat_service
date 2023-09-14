package com.websocket.demo.controller;

import com.websocket.demo.request.ChatRequest;
import com.websocket.demo.response.ChatInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Controller
public class ChatController {

    @MessageMapping("/chat")
    @SendTo("/topic/chat")
    public ChatInfo greeting(ChatRequest request){
        var chatInfo = new ChatInfo();
        chatInfo.setMessage(request.getMessage());
//        chatInfo.setSender(userInfo.getNickname());
        return chatInfo;
    }

}
