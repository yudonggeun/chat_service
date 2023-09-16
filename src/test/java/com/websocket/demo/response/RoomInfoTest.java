package com.websocket.demo.response;

import com.websocket.demo.domain.Chat;
import com.websocket.demo.domain.Room;
import com.websocket.demo.domain.RoomUserData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;

class RoomInfoTest {

    @DisplayName("채팅방의 정보를 담은 객체를 생성시 모든 정보가 일치해야한다.")
    @Test
    public void convertRoomInfoInfo() {
        //given
        var room = spy(Room.builder()
                .title("rooms")
                .build());
        List<Chat> chatList = new LinkedList<>();
        List<RoomUserData> roomUserDataList = List.of(
                createRoomData(room, "nick", "white", 10L),
                createRoomData(room, "john", "white", 11L)
        );
        given(room.getId()).willReturn(100L);
        given(room.getData()).willReturn(List.of());
        given(room.getChatList()).willReturn(chatList);
        given(room.getData()).willReturn(roomUserDataList);

        var chat = spy(Chat.builder()
                .room(room)
                .message("hello world")
                .senderNickname("nick")
                .build());

        given(chat.getId()).willReturn(1L);
        given(chat.getCreatedAt()).willReturn(LocalDateTime.of(2000, 10, 10, 0, 0, 0));
        chatList.add(chat);
        //when
        var roomInfo = RoomInfo.from(room);
        //then
        assertThat(roomInfo)
                .extracting("id", "title")
                .containsExactly(100L, "rooms");

        assertThat(roomInfo.getChat()).extracting("message", "sender", "createdAt", "roomId")
                .contains(
                        tuple("hello world", "nick", chat.getCreatedAt(), 100L)
                );

        assertThat(roomInfo.getUsers()).contains("john", "nick");
    }

    private static RoomUserData createRoomData(Room room, String nickname, String background, long id) {
        RoomUserData result = spy(RoomUserData.builder()
                .room(room)
                .userNickname(nickname)
                .backgroundColor(background)
                .build());
        given(result.getId()).willReturn(id);
        return result;
    }
}