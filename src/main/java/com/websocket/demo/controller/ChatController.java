package com.websocket.demo.controller;

import com.websocket.demo.request.ChatRequest;
import com.websocket.demo.response.ChatInfo;
import com.websocket.demo.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/chat/new")
    @SendTo("/topic/chat/new")
    public ChatInfo newChat(ChatRequest request){
        return chatService.createChat(request);
    }
}