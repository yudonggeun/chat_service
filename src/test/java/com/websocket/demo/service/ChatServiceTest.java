package com.websocket.demo.service;

import com.websocket.demo.domain.Chat;
import com.websocket.demo.repository.ChatRepository;
import com.websocket.demo.request.ChatRequest;
import com.websocket.demo.response.ChatInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class ChatServiceTest {

    ChatService chatService;
    ChatRepository chatRepository;

    @BeforeEach
    void init() {
        chatRepository = mock(ChatRepository.class);
        chatService = new ChatService(chatRepository);
    }

    @DisplayName("체팅을 전달 받으면 저장하고 저장된 체팅 정보를 반환한다.")
    @Test
    public void createChatTest() {
        //given
        var request = new ChatRequest();
        request.setSender("hello");
        request.setMessage("welcome to the chat service");
        request.setRoomId(100L);

        given(chatRepository.save(any()))
                .willAnswer(invocation -> invocation.getArgument(0, Chat.class));
        //when
        ChatInfo info = chatService.createChat(request);
        //then
        assertThat(info).extracting("sender", "message", "roomId")
                .containsExactly("hello", "welcome to the chat service", 100L);
    }

    @DisplayName("체팅을 전달 받고 저장이 실패하면 오류가 발생한다.")
    @Test
    public void createChatFail() {
        //given
        var request = new ChatRequest();
        request.setSender("hello");
        request.setMessage("welcome to the chat service");

        given(chatRepository.save(any())).willThrow(RuntimeException.class);
        //when //then
        assertThatThrownBy(() -> chatService.createChat(request));
    }
}