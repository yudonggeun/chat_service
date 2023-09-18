package com.websocket.demo.controller;

import com.websocket.demo.request.*;
import com.websocket.demo.response.ChatStompResponse;
import com.websocket.demo.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebsocketController {

    private final ChatService chatService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat/new")
    public void newChat(CreateChatRequest request) {
        var message = ChatStompResponse.createChat(chatService.createChat(request));
        sendTo(request.getRoomId(), message);
    }

    @MessageMapping("/chat/delete")
    public void deleteChat(DeleteChatRequest request) {
        var message = ChatStompResponse.deleteChat(chatService.delete(request));
        sendTo(request.getRoomId(), message);
    }

    @MessageMapping("/room/out")
    public void roomOut(RoomOutRequest request, SimpMessageHeaderAccessor accessor) {
        var nickname = (String) accessor.getSessionAttributes().get("nickname");
        var message = ChatStompResponse.getOutRoom(chatService.getOutRoom(request, nickname));
        sendTo(request.getId(), message);
    }

    @MessageMapping("/room/in")
    public void roomOut(InViteUserRequest request, SimpMessageHeaderAccessor accessor) {
        var host = (String) accessor.getSessionAttributes().get("nickname");
        var message = ChatStompResponse.friendComeInRoom(chatService.inviteUser(request, host));
        sendTo(request.getRoomId(), message);
        sendTo(request.getNickname(), message);
    }

    @MessageMapping("/room/check")
    public void readChat(CheckRoomRequest request, SimpMessageHeaderAccessor accessor){
        var host = (String) accessor.getSessionAttributes().get("nickname");
        var message = ChatStompResponse.readChat(chatService.checkRoom(request, host));
        sendTo(request.getRoomId(), message);
    }

    private void sendTo(Long roomId, Object message){
        simpMessagingTemplate.convertAndSend("/topic/chat-" + roomId, message);
    }

    private void sendTo(String nickname, Object message){
        simpMessagingTemplate.convertAndSend("/topic/chat-" + nickname, message);
    }
}