package com.websocket.demo.response;

import com.websocket.demo.domain.Chat;
import com.websocket.demo.domain.Room;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class ChatInfoTest {

    @DisplayName("체팅의 정보를 동일하게 담고 있다.")
    @Test
    public void from() {
        //given
        Room room = spy(Room.builder().build());
        Chat chat = spy(Chat.builder()
                .message("message")
                .senderNickname("sender")
                .room(room)
                .build());
        LocalDateTime time = LocalDateTime.now();
        given(chat.getId()).willReturn(1L);
        given(chat.getCreatedAt()).willReturn(time);
        given(room.getId()).willReturn(100L);
        //when
        ChatInfo info = ChatInfo.from(chat);
        //then
        assertThat(info).extracting("id", "sender", "message", "roomId", "createdAt")
                .containsExactly(1L, "sender", "message", 100L, time);
    }
}