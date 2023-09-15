package com.websocket.demo.controller;

import com.websocket.demo.request.CreateChatRequest;
import com.websocket.demo.request.DeleteChatRequest;
import com.websocket.demo.response.ChatInfo;
import com.websocket.demo.response.DeleteChat;
import com.websocket.demo.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/chat/new")
    @SendTo("/topic/chat/new")
    public ChatInfo newChat(CreateChatRequest request) {
        return chatService.create(request);
    }

    @MessageMapping("/chat/delete")
    @SendTo("/topic/chat/delete")
    public DeleteChat deleteChat(DeleteChatRequest request) {
        return chatService.delete(request);
    }

}