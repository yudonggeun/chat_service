package com.websocket.demo.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @DisplayName("유저의 닉네임과 비밀번호를 조회할 수 있다.")
    @Test
    public void getNickName() {
        //given //when
        var nickname = "test1324";
        var password = "my password";
        var user = new User(nickname, password);
        //then
        assertThat(user.getNickname()).isEqualTo(nickname);
        assertThat(user.getPassword()).isEqualTo(password);
    }

}