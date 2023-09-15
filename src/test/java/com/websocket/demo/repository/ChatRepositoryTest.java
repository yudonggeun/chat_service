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
        assertThat(saved.getCreateAt()).isNotNull();
    }

    @DisplayName("주어진 id의 체팅을 삭제한다.")
    @Test
    public void deleteOneById() {
        //given
        Chat chat = chatRepository.save(Chat.builder()
                .roomId(100L)
                .senderNickname("hello")
                .message("hi")
                .build());
        //when
        chatRepository.deleteById(100L);
        //then
        assertThat(chatRepository.findById(100L)).isEmpty();
    }
}