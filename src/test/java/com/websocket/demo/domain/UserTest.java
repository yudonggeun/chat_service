package com.websocket.demo.domain;

import com.websocket.demo.request.LoginRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

    @DisplayName("로그인 요청 정보가 일치하면 true를 반환한다.")
    @Test
    public void matchLoginWhenSuccess() {
        //given
        var user = new User("test1234", "my password");
        var request = new LoginRequest();
        request.setNickname("test1234");
        request.setPassword("my password");
        //when //then
        assertThat(user.match(request)).isTrue();
    }

    @DisplayName("로그인 요청 정보(비밀번호)가 일치하지 않으면 false를 반환한다.")
    @Test
    public void matchLoginWhenFailCaseThatPasswordIsNotCorrect() {
        //given
        var user = new User("test1324", "my password");
        var request = new LoginRequest();
        request.setNickname("test1234");
        request.setPassword("my passworddfkdfd");
        //when //then
        assertThat(user.match(request)).isFalse();
    }

    @DisplayName("로그인 요청 정보(닉네임)이 일치하지 않으면 false를 반환한다.")
    @Test
    public void matchLoginWhenFailCaseUserNotFound() {
        //given
        var user = new User("test1324", "my password");
        var request = new LoginRequest();
        request.setNickname("test");
        request.setPassword("my password");
        //when //then
        assertThat(user.match(request)).isFalse();
    }
}