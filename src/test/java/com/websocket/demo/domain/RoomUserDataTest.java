package com.websocket.demo.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class RoomUserDataTest {

    @DisplayName("채팅방 배경색 변경이 반영된다.")
    @ParameterizedTest
    @ValueSource(strings = {"blue", "white", "red", "green"})
    public void setBackground(String color) {
        //given
        var entity = RoomUserData.builder().build();
        //when
        entity.setBackgroundColor(color);
        //then
        assertThat(entity.getBackgroundColor()).isEqualTo(color);
    }

    @DisplayName("확인 시간을 변경할 수 있다.")
    @Test
    public void setTime() {
        //given
        var entity = RoomUserData.builder().build();
        var time = LocalDateTime.now();
        //when
        entity.setCheckTime(time);
        //then
        assertThat(entity.getCheckTime()).isEqualTo(time);
    }
}