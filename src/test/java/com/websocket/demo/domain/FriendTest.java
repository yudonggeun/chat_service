package com.websocket.demo.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FriendTest {

    @DisplayName("친구 목록은 빌더를 사용해서 만들 수 있다.")
    @Test
    public void createByBuilder() {
        //given
        var user = new User("my", "1234");
        var friendId = 100l;
        var friendInfo = Friend.builder().friendId(friendId).user(user).build();
        //when //then
        assertThat(friendInfo.getUser()).isEqualTo(user);
        assertThat(friendInfo.getFriendId()).isEqualTo(friendId);
    }

}