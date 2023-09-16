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

}