package com.websocket.demo.service;

import com.websocket.demo.domain.Chat;
import com.websocket.demo.repository.ChatRepository;
import com.websocket.demo.request.CreateChatRequest;
import com.websocket.demo.request.DeleteChatRequest;
import com.websocket.demo.response.ChatInfo;
import com.websocket.demo.response.DeleteChat;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final ChatRepository chatRepository;

    public ChatInfo create(CreateChatRequest request) {
        var chat = chatRepository.saveAndFlush(Chat.builder()
                .senderNickname(request.getSender())
                .roomId(request.getRoomId())
                .message(request.getMessage())
                .build()
        );
        return ChatInfo.from(chat);
    }

    public DeleteChat delete(DeleteChatRequest request) {
        chatRepository.deleteById(request.getId());
        return new DeleteChat(request.getRoomId(), request.getId());
    }
}
