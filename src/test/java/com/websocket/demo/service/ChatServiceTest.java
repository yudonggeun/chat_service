package com.websocket.demo.service;

import com.websocket.demo.SpringTest;
import com.websocket.demo.domain.Chat;
import com.websocket.demo.domain.Room;
import com.websocket.demo.repository.ChatRepository;
import com.websocket.demo.repository.RoomRepository;
import com.websocket.demo.request.CreateChatRequest;
import com.websocket.demo.request.DeleteChatRequest;
import com.websocket.demo.request.FindChatListRequest;
import com.websocket.demo.response.ChatInfo;
import com.websocket.demo.response.DeleteChat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ChatServiceTest extends SpringTest {

    @Autowired
    ChatService chatService;
    @Autowired
    ChatRepository chatRepository;
    @Autowired
    RoomRepository roomRepository;

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

    @DisplayName("조건에 맞는 채팅 리스트를 찾는다.")
    @Test
    public void findChatList() {
        //given
        Room room = roomRepository.saveAndFlush(Room.builder()
                .title("chatting room 1")
                .build());
        saveChat(room.getId(), "nick", "hello");
        saveChat(room.getId(), "tom", "bye");

        var request = new FindChatListRequest();
        request.setRoomId(room.getId());
        request.setFrom(LocalDateTime.of(1999, 1, 1, 0, 0, 0));
        request.setTo(LocalDateTime.of(2030, 1, 1, 0, 0, 0));
        //when
        List<ChatInfo> result = chatService.findChatList(request);
        //then
        assertThat(result).extracting("sender", "message", "roomId")
                .containsExactly(
                        tuple("nick", "hello", room.getId()),
                        tuple("tom", "bye", room.getId())
                );
    }

    private Chat saveChat(Long roomId, String sender, String message) {
        return chatRepository.saveAndFlush(Chat.builder()
                .message(message)
                .senderNickname(sender)
                .roomId(roomId)
                .build());
    }
}