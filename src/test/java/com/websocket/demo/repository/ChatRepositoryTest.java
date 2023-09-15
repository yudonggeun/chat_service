package com.websocket.demo.repository;

import com.websocket.demo.SpringTest;
import com.websocket.demo.domain.Chat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class ChatRepositoryTest extends SpringTest {

    @Autowired
    ChatRepository chatRepository;
    @DisplayName("체팅이 저장된다.")
    @Test
    public void saveChat() {
        //given
        Chat chat = Chat.builder()
                .roomId(100L)
                .senderNickname("hello")
                .message("hi")
                .build();
        //when
        Chat saved = chatRepository.save(chat);
        //then
        assertThat(saved).isNotNull()
                .extracting("roomId", "senderNickname", "message")
                .containsExactly(100L, "hello", "hi");
        assertThat(saved.getId()).isNotNull();
    }
}