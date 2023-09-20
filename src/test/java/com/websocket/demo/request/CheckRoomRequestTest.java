package com.websocket.demo.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CheckRoomRequestTest {

    @DisplayName("시간을 설정하지 않는다면 객체가 생성된 시간을 지닌다.")
    @Test
    public void initTime() {
        //given
        var object = new CheckRoomRequest();
        //when //then
        assertThat(object.getCheckTime()).isNotNull();
    }
}