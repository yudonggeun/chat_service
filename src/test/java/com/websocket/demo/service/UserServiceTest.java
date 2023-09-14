package com.websocket.demo.service;

import com.websocket.demo.SpringTest;
import com.websocket.demo.domain.User;
import com.websocket.demo.repository.UserRepository;
import com.websocket.demo.request.AddFriendRequest;
import com.websocket.demo.request.CreateUserRequest;
import com.websocket.demo.request.LoginRequest;
import com.websocket.demo.response.FriendInfo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserServiceTest extends SpringTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @BeforeEach
    @AfterEach
    void init() {
        userRepository.deleteAll();
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
            //when //then
            assertThat(userService.create(request)).isFalse();
        }
    }

    @Nested
    @DisplayName("로그인 요청시")
    class login {
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

    @DisplayName("친구 추가할 때")
    @Nested
    class WhenAddFriend {

        @BeforeEach
        void init() {
            saveUser("hello", "1234");
            saveUser("friend1", "1234");
        }

        @Transactional
        @DisplayName("올바른 친구 닉네임이고 로그인이 되었을 때")
        @Test
        public void successCase() {
            //given
            var loginUserNickname = "hello";
            var request = new AddFriendRequest();
            request.setNickname("friend1");
            //when
            boolean result = userService.addFriend(request, loginUserNickname);
            //then
            assertThat(result).isTrue();
            assertThat(userService.friendList(loginUserNickname)).hasSize(1);
        }

        @DisplayName("친구 닉네임이 존재하지 않을때")
        @Test
        public void failCase1() {
            //given
            var loginUserNickname = "hello";
            var request = new AddFriendRequest();
            request.setNickname("friend2");
            //when
            boolean result = userService.addFriend(request, loginUserNickname);
            //then
            assertThat(result).isFalse();
        }

        @DisplayName("로그인하지 않았을 때")
        @Test
        public void failCase2() {
            //given
            var request = new AddFriendRequest();
            request.setNickname("friend1");
            //when
            boolean result = userService.addFriend(request, null);
            //then
            assertThat(result).isFalse();
        }
    }

    @DisplayName("유저의 친구 목록을 조회할 수 있다.")
    @Test
    public void friendList() {
        //given
        User user = saveUser("hello", "1234");

        User friend1= saveUser("friend1", "1234");
        User friend2= saveUser("friend2", "1234");

        user.addFriends(friend1);
        user.addFriends(friend2);
        userRepository.saveAndFlush(user);
        //when
        List<FriendInfo> friends = userService.friendList(user.getNickname());

        //then
        assertThat(friends).extracting("nickname").containsExactly(friend1.getNickname(), friend2.getNickname());
    }

    @DisplayName("친구를 삭제시 친구 목록에서 제외된다.")
    @Test
    public void deleteFriend() {
        //given
        User user = saveUser("hello", "1234");

        User friend1= saveUser("friend1", "1234");
        User friend2= saveUser("friend2", "1234");

        user.addFriends(friend1);
        user.addFriends(friend2);
        userRepository.saveAndFlush(user);
        //when
        userService.removeFriendByNickname(user.getNickname(), friend1.getNickname());
        //then
        assertThat(userService.friendList(user.getNickname()))
                .extracting("nickname")
                .contains(friend2.getNickname())
                .doesNotContain(friend1.getNickname());
    }

    private User saveUser(String nickname, String password) {
        return userRepository.saveAndFlush(
                User.builder()
                        .nickname(nickname)
                        .password(password).build()
        );
    }
}