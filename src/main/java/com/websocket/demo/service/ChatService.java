package com.websocket.demo.service;

import com.websocket.demo.domain.Chat;
import com.websocket.demo.repository.ChatRepository;
import com.websocket.demo.request.ChatRequest;
import com.websocket.demo.response.ChatInfo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    public ChatInfo createChat(ChatRequest request) {
        var chat = chatRepository.save(Chat.builder()
                .senderNickname(request.getSender())
                .roomId(request.getRoomId())
                .message(request.getMessage())
                .build()
        );
        return ChatInfo.from(chat);
    }
}
