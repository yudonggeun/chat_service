package com.websocket.demo.service;

import com.websocket.demo.SpringTest;
import com.websocket.demo.domain.Chat;
import com.websocket.demo.domain.Room;
import com.websocket.demo.repository.ChatRepository;
import com.websocket.demo.repository.RoomInfoRepository;
import com.websocket.demo.repository.RoomRepository;
import com.websocket.demo.request.*;
import com.websocket.demo.response.ChatInfo;
import com.websocket.demo.response.DeleteChat;
import com.websocket.demo.response.RoomInfo;
import com.websocket.demo.response.RoomUserInfo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
    @Autowired
    RoomInfoRepository roomInfoRepository;

    @BeforeEach
    public void init() {
        chatRepository.deleteAllInBatch();
        roomInfoRepository.deleteAllInBatch();
        roomRepository.deleteAllInBatch();
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
        var room = saveRoom("room test", "nick", "jon");
        var chat = saveChat(room, "nick", "hello");

        var request = new DeleteChatRequest();
        request.setId(chat.getId());
        request.setRoomId(room.getId());
        //when
        DeleteChat response = chatService.deleteChat(request, "nick");
        // then
        assertThat(response).extracting("id", "roomId")
                .containsExactly(chat.getId(), room.getId());
    }

    @DisplayName("체팅 삭제가 실패한다면 예외가 발생한다")
    @Test
    public void deleteChatFail() {
        //given
        var request = new DeleteChatRequest();
        //when //then
        assertThatThrownBy(() -> chatService.deleteChat(request, ""))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("체팅 삭제 요청시 자신이 작성한 채팅이 아니라면 예외가 발생한다")
    @Test
    public void deleteChatFailWhenNotAllow() {
        //given
        var host = "jon";
        var room = saveRoom("room test", "nick", "jon");
        var chat = saveChat(room, "nick", "hello");

        var request = new DeleteChatRequest();
        request.setId(chat.getId());
        request.setRoomId(room.getId());
        //when //then
        assertThatThrownBy(() -> chatService.deleteChat(request, host))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("체팅 삭제 요청시 채팅방 id가 다르면 예외가 발생한다")
    @Test
    public void deleteChatFailWhenNotMatchRoomId() {
        //given
        var host = "nick";
        var room = saveRoom("room test", "nick", "jon");
        var chat = saveChat(room, "nick", "hello");

        var request = new DeleteChatRequest();
        request.setId(chat.getId());
        request.setRoomId(room.getId() + 1);
        //when //then
        assertThatThrownBy(() -> chatService.deleteChat(request, host))
                .isInstanceOf(IllegalArgumentException.class);
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

    @DisplayName("채팅방을 나간다.")
    @Test
    public void getOutRoom() {
        //given
        Long id = saveRoom("room1", "nick", "john").getId();

        var request = new RoomOutRequest();
        request.setId(id);
        var nickname = "nick";
        //when
        chatService.getOutRoom(request, nickname);
        //then
        assertThat(roomInfoRepository.findAll()).extracting("userNickname", "id")
                .doesNotContain(tuple("nick", id));
        assertThat(roomRepository.findById(id)).isNotEmpty();
    }

    @DisplayName("채팅방을 나가고 아무도 없는 채팅방이라면 채팅방을 삭제한다.")
    @Test
    public void getOutRoomAndRemoveRoom() {
        //given
        Long id = saveRoom("room1", "nick").getId();

        var request = new RoomOutRequest();
        request.setId(id);
        var nickname = "nick";
        //when
        chatService.getOutRoom(request, nickname);
        //then
        assertThat(roomRepository.findById(id)).isEmpty();
    }

    @DisplayName("없는 채팅방에 초대하면 예외가 발생한다.")
    @Test
    public void inviteUserFakeRoom() {
        //given
        var request = new InViteUserRequest();
        request.setRoomId(100L);
        //when //then
        assertThatThrownBy(() -> chatService.inviteUser(request, "kun"));
    }

    @DisplayName("채팅방에 소속되지 않는 유저가 초대하면 예외가 발생한다.")
    @Test
    public void inviteUserStrangeHost() {
        //given
        Room room = saveRoom("room1", "kim");
        var request = new InViteUserRequest();
        request.setRoomId(room.getId());
        request.setNickname("kun");
        //when //then
        assertThatThrownBy(() -> chatService.inviteUser(request, "cul"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("초대 유저가 null 이라면 예외가 발생한다.")
    @Test
    public void inviteUserHostNull() {
        //given
        Room room = saveRoom("room1", "kim");
        var request = new InViteUserRequest();
        request.setRoomId(room.getId());
        request.setNickname("kun");
        //when //then
        assertThatThrownBy(() -> chatService.inviteUser(request, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Transactional
    @DisplayName("채팅방 초대 요청시 성공한다")
    @Test
    public void inviteUser() {
        //given
        Room room = saveRoom("room1", "kim");
        var request = new InViteUserRequest();
        request.setRoomId(room.getId());
        request.setNickname("kun");
        //when
        RoomInfo result = chatService.inviteUser(request, "kim");
        var savedRoom = roomRepository.findById(room.getId()).get();
        //then
        assertThat(result.getId()).isEqualTo(room.getId());
        assertThat(result.getUsers()).contains("kun", "kim");
        assertThat(savedRoom.containsUser("kun")).isTrue();
        assertThat(savedRoom.containsUser("kim")).isTrue();
    }

    @Transactional
    @DisplayName("채팅방 초대시 이미 있는 유저라면 예외가 발생한다.")
    @Test
    public void inviteUserWhenDuplicate() {
        //given
        Room room = saveRoom("room1", "kim");
        var request = new InViteUserRequest();
        request.setRoomId(room.getId());
        request.setNickname("kun");
        //when
        RoomInfo result = chatService.inviteUser(request, "kim");
        //then
        assertThatThrownBy(() -> chatService.inviteUser(request, "kim"));
    }

    @ParameterizedTest
    @DisplayName("채팅방 설정을 변경한다.")
    @ValueSource(strings = {"blue", "white", "red", "green"})
    public void updateRoom(String color) {
        //given
        Long id = saveRoom("test", "mark").getId();

        var request = new UpdateRoomConfigRequest();
        request.setBackgroundColor(color);
        request.setRoomId(id);
        //when
        RoomUserInfo roomUserInfo = chatService.updateRoom(request, "mark");
        //then
        assertThat(roomUserInfo).extracting("roomId", "nickname", "backgroundColor", "time")
                .containsExactly(id, "mark", color, null);
        assertThat(roomInfoRepository.findByUserNicknameAndRoomId("mark", id).get())
                .extracting("userNickname", "backgroundColor")
                .containsExactly("mark", color);
    }

    @DisplayName("설정하고자하는 채팅방이 없다면 예외가 발생한다.")
    @Test
    public void updateRoomThatNotExist() {
        //given
        Long id = 1L;
        String color = "red";

        var request = new UpdateRoomConfigRequest();
        request.setBackgroundColor(color);
        request.setRoomId(id);
        //when //then
        assertThatThrownBy(() -> chatService.updateRoom(request, "mark"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("채팅방에 유저가소속되지 않다면 예외가 발생한다.")
    @Test
    public void updateRoomButUserNotValid() {
        //given
        Long id = saveRoom("test", "mark").getId();
        String color = "blue";

        var request = new UpdateRoomConfigRequest();
        request.setBackgroundColor(color);
        request.setRoomId(id);
        //when //then
        assertThatThrownBy(() -> chatService.updateRoom(request, "yan"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("채팅방을 확인하면 확인 시간을 갱신하고 그 정보를 반환한다.")
    @Test
    public void checkRoom() {
        //given
        Room room = saveRoom("room test", "nick");
        var checkTime = LocalDateTime.now();
        var request = new CheckRoomRequest();
        request.setRoomId(room.getId());
        request.setCheckTime(checkTime);
        var host = "nick";
        //when
        RoomUserInfo info = chatService.checkRoom(request, host);
        //then
        assertThat(info).extracting("roomId", "nickname", "time", "backgroundColor")
                .containsExactly(room.getId(), "nick", checkTime, null);
    }

    @DisplayName("채팅 확인시 채팅방이 존재하지 않는다면 예외가 발생한다.")
    @Test
    public void checkRoomFail() {
        //given
        var checkTime = LocalDateTime.now();
        var request = new CheckRoomRequest();
        request.setRoomId(100L);
        request.setCheckTime(checkTime);
        var host = "nick";
        //when //then
        assertThatThrownBy(() -> chatService.checkRoom(request, host))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("채팅확인시 채팅방에 소속되지 않은 유저라면 예외가 발생한다.")
    @Test
    public void checkRoomFail2() {
        //given
        var room = saveRoom("test room", "kal");
        var checkTime = LocalDateTime.now();
        var request = new CheckRoomRequest();
        request.setRoomId(room.getId());
        request.setCheckTime(checkTime);
        var host = "nick";
        //when //then
        assertThatThrownBy(() -> chatService.checkRoom(request, host))
                .isInstanceOf(IllegalArgumentException.class);
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