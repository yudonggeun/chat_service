package com.websocket.demo.response;

import com.websocket.demo.domain.Chat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

class ChatInfoTest {

    @DisplayName("체팅의 정보를 동일하게 담고 있다.")
    @Test
    public void from() {
        //given
        Chat chat = Mockito.spy(Chat.builder()
                .message("message")
                .senderNickname("sender")
                .roomId(100L)
                .build());
        given(chat.getId()).willReturn(1L);
        //when
        ChatInfo info = ChatInfo.from(chat);
        //then
        assertThat(info).extracting("id", "sender", "message", "roomId")
                .containsExactly(1L, "sender", "message", 100L);
    }
}