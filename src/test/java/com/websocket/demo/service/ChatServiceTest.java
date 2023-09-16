package com.websocket.demo.service;

import com.websocket.demo.SpringTest;
import com.websocket.demo.domain.Chat;
import com.websocket.demo.domain.Room;
import com.websocket.demo.repository.ChatRepository;
import com.websocket.demo.repository.RoomRepository;
import com.websocket.demo.request.CreateChatRequest;
import com.websocket.demo.request.CreateRoomRequest;
import com.websocket.demo.request.DeleteChatRequest;
import com.websocket.demo.request.FindChatListRequest;
import com.websocket.demo.response.ChatInfo;
import com.websocket.demo.response.DeleteChat;
import com.websocket.demo.response.RoomInfo;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    public void init(){
        chatRepository.deleteAllInBatch();
        roomRepository.deleteAll();
    }

    @DisplayName("체팅을 전달 받으면 저장하고 저장된 체팅 정보를 반환한다.")
    @Test
    public void createChatTest() {
        //given
        var room = saveRoom("room1", "john");
        var request = new CreateChatRequest();
        request.setSender("hello");
        request.setMessage("welcome to the chat service");
        request.setRoomId(room.getId());
        //when
        ChatInfo info = chatService.createChat(request);
        //then
        assertThat(info)
                .extracting("sender", "message", "roomId")
                .containsExactly("hello", "welcome to the chat service", room.getId());
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
        assertThatThrownBy(() -> chatService.createChat(request));
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
        Room room = saveRoom("chatting room 1", "john");
        saveChat(room, "nick", "hello");
        saveChat(room, "tom", "bye");

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

    @DisplayName("특정 유저의 채팅방 목록을 전체 조회한다.")
    @Test
    public void findRoomList() {
        //given
        saveRoom("room1", "nick");
        saveRoom("room2", "nick");
        saveRoom("room3", "john");
        //when
        List<RoomInfo> rooms = chatService.findRoomList("nick");
        //then
        assertThat(rooms).extracting("title")
                .contains("room1", "room2");
    }

    @DisplayName("특정 유저와 일치하는 체팅방이 없다면 빈 배열을 반환한다.")
    @Test
    public void findRoomListFail() {
        //given
        saveRoom("only nick room", "nick");
        //when
        List<RoomInfo> rooms = chatService.findRoomList("john");
        //then
        assertThat(rooms).isEmpty();
    }

    @DisplayName("채팅방을 생성한다.")
    @Test
    public void createRoom() {
        //given
        var request = new CreateRoomRequest();
        request.setTitle("my room");
        request.setUsers(List.of("john", "tom", "nick"));
        //when
        RoomInfo room = chatService.createRoom(request);
        //then
        assertThat(room.getTitle()).isEqualTo("my room");
        assertThat(room.getId()).isNotNull();
        assertThat(room.getUsers()).contains("john", "tom", "nick");
        assertThat(room.getChat()).isEmpty();
    }

    private Chat saveChat(Room room, String sender, String message) {
        return chatRepository.saveAndFlush(Chat.builder()
                .message(message)
                .senderNickname(sender)
                .room(room)
                .build());
    }

    private Room saveRoom(String title, String... userNicknames) {
        Room room = Room.builder()
                .title(title)
                .build();

        for (String nickname : userNicknames) {
            room.addUser(nickname);
        }

        return roomRepository.saveAndFlush(room);
    }
}