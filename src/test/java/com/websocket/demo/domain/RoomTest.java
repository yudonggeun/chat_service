package com.websocket.demo.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class RoomTest {

    @DisplayName("닉네임으로 유저를 추가할 수 있다")
    @Test
    public void addUser() {
        //given
        Room room = Room.builder()
                .title("room1")
                .build();
        //when
        room.addUser("nick");
        room.addUser("ann");
        //then
        assertThat(room.getData())
                .extracting("userNickname", "backgroundColor", "room")
                .contains(
                        tuple("nick", "white", room),
                        tuple("ann", "white", room)
                );
    }

    @DisplayName("닉네임으로 유저 추가시 해당 유저의 채팅방 설정 정보를 반환한다.")
    @Test
    public void addUserReturn() {
        //given
        Room room = Room.builder()
                .title("room1")
                .build();
        //when
        RoomUserData data = room.addUser("nick");
        //then
        assertThat(data).extracting("room", "backgroundColor", "userNickname")
                .containsExactly(room, "white", "nick");
    }

    @DisplayName("유저가 채팅방에 소속되어있는 여부를 반환한다.")
    @Test
    public void containsUser() {
        //given
        Room room = Room.builder()
                .title("room1")
                .build();
        room.addUser("nick");
        room.addUser("ann");
        //when //then
        assertThat(room.containsUser("nick")).isTrue();
        assertThat(room.containsUser("hong")).isFalse();
    }

}