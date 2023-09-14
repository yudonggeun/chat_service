package com.websocket.demo.service;

import com.websocket.demo.SpringTest;
import com.websocket.demo.domain.User;
import com.websocket.demo.repository.UserRepository;
import com.websocket.demo.request.CreateUserRequest;
import com.websocket.demo.request.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceTest extends SpringTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @BeforeEach
    void init(){
        userRepository.deleteAllInBatch();
    }
    @DisplayName("회원가입 요청이 오면")
    @Nested
    class WhenCreateUserRequest {

        @DisplayName("닉네임이 중복되지 않는다면 회원가입시킨다.")
        @Test
        public void isNotDuplicatedThenCreateUser() {
            //given
            var request = new CreateUserRequest();
            request.setNickname("newHello");
            request.setPassword("1234");
            //when
            boolean result = userService.create(request);
            //then
            assertThat(result).isTrue();
        }

        @DisplayName("닉네임이 중복되면 회원가입을 시키지 않는다.")
        @Test
        public void isDuplicatedThenDoingNoting() {
            //given
            saveUser("hello", "124");
            var request = new CreateUserRequest();
            request.setNickname("hello");
            request.setPassword("1234");
            //when
            boolean result = userService.create(request);
            //then
            assertThat(result).isFalse();
        }
    }

    @DisplayName("로그인 요청이 오면")
    @Nested
    class WhenLoginRequest{

        @DisplayName("닉네임과 비밀번호가 일치하면 로그인 성공")
        @Test
        public void successLogin() {
            //given
            saveUser("hello", "1234");
            var request = new LoginRequest();
            request.setNickname("hello");
            request.setPassword("1234");
            //when
            boolean isSuccess = userService.login(request);
            //then
            assertThat(isSuccess).isTrue();
        }

        @DisplayName("닉네임과 비밀번호가 일치하지 않으면 로그인 실패")
        @Test
        public void failLogin() {
            //given
            saveUser("hello", "124");
            var request = new LoginRequest();
            request.setNickname("hello");
            request.setPassword("1234");
            //when
            boolean isSuccess = userService.login(request);
            //then
            assertThat(isSuccess).isFalse();
        }
    }

    private void saveUser(String nickname, String password) {
        userRepository.saveAndFlush(
                User.builder()
                        .nickname(nickname)
                        .password(password).build()
        );
    }
}