package com.websocket.demo.response;

import com.websocket.demo.domain.Room;
import com.websocket.demo.domain.RoomUserData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.mockito.Mockito.spy;

class RoomUserInfoTest {

    @DisplayName("채팅방 정보가 info 객체로 정확하게 전달된다.")
    @Test
    public void createRoomUserInfoFromEntity() {
        //given
        var id = 100L;
        var time = LocalDateTime.of(2000, 10, 11, 1, 23, 40);
        var room = mock(Room.class);

        RoomUserData data = spy(RoomUserData.builder()
                .room(room)
                .userNickname("nick")
                .backgroundColor("white")
                .build());

        given(room.getId()).willReturn(id);
        given(data.getCheckTime()).willReturn(time);
        //when
        RoomUserInfo from = RoomUserInfo.from(data);
        //then
        assertThat(from).extracting("roomId", "nickname", "time", "backgroundColor")
                .containsExactly(id, "nick", time, "white");
    }
}