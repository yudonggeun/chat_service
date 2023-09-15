package com.websocket.demo.service;

import com.websocket.demo.SpringTest;
import com.websocket.demo.repository.ChatRepository;
import com.websocket.demo.request.CreateChatRequest;
import com.websocket.demo.request.DeleteChatRequest;
import com.websocket.demo.response.ChatInfo;
import com.websocket.demo.response.DeleteChat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ChatServiceTest extends SpringTest {

    @Autowired
    ChatService chatService;
    @Autowired
    ChatRepository chatRepository;

    @DisplayName("체팅을 전달 받으면 저장하고 저장된 체팅 정보를 반환한다.")
    @Test
    public void createChatTest() {
        //given
        var request = new CreateChatRequest();
        request.setSender("hello");
        request.setMessage("welcome to the chat service");
        request.setRoomId(100L);
        //when
        ChatInfo info = chatService.create(request);
        //then
        assertThat(info)
                .extracting("sender", "message", "roomId")
                .containsExactly("hello", "welcome to the chat service", 100L);
        assertThat(info.getId()).isNotNull();
    }

    @DisplayName("체팅을 전달 받고 저장이 실패하면 오류가 발생한다.")
    @Test
    public void createChatFail() {
        //given
        var request = new CreateChatRequest();
        request.setSender("hello");
        request.setMessage("welcome to the chat service");
        //when //then
        assertThatThrownBy(() -> chatService.create(request));
    }
    @DisplayName("체팅 삭제 성공할 때 예외발생하지 않는다.")
    @Test
    public void deleteChatSuccess() {
        //given
        var request = new DeleteChatRequest();
        request.setId(100L);
        request.setRoomId(10L);
        //when
        DeleteChat response = chatService.delete(request);
        // then
        assertThat(response).extracting("id", "roomId")
                .containsExactly(100L, 10L);
    }
    @DisplayName("체팅 삭제가 실패한다면 예외가 발생한다")
    @Test
    public void deleteChatFail() {
        //given
        var request = new DeleteChatRequest();
        //when //then
        assertThatThrownBy(() -> chatService.delete(request))
                .isInstanceOf(RuntimeException.class);
    }
}