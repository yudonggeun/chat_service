package com.websocket.demo.repository;

import com.websocket.demo.domain.Friend;
import com.websocket.demo.domain.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @DisplayName("유저가 저장된다.")
    @Test
    public void save() {
        //given
        var nickname = "test1324";
        var password = "my password";
        var user = saveUser(nickname, password);
        //when
        User savedUser = userRepository.saveAndFlush(user);
        //then
        assertThat(savedUser).isNotNull();
    }

    @DisplayName("유저의 친구 목록을 조회할 수 있다.")
    @Test
    public void getFriendsList() {
        //given
        var user = saveUser("user1", "1234");
        var friend1 = saveUser("friend1", "1234");
        var friend2 = saveUser("friend2", "1234");
        user.addFriends(friend1, friend2);
        //when
        List<Friend> friends = user.getFriends();
        //then
        assertThat(friends).extracting("friend.nickname")
                .containsExactly(friend1.getNickname(), friend2.getNickname());
    }

    @DisplayName("유저 닉네임을 통해서 유저 엔티티를 조회할 수 있다.")
    @Test
    public void findByNickname() {
        //given
        User user = saveUser("testNick", "1234");
        //when
        User findUser = userRepository.findByNickname("testNick");
        //then
        assertThat(findUser).isNotNull()
                .extracting("nickname", "password")
                .containsExactly("testNick", "1234");
    }

    @DisplayName("닉네임 중복 여부를 검사한다.")
    @Test
    public void existsByNickname() {
        //given
        User user = saveUser("testNick", "1234");
        //when
        boolean result = userRepository.existsByNickname(user.getNickname());
        //then
        assertThat(result).isTrue();
    }
    private User saveUser(String nickname, String password) {
        return userRepository.saveAndFlush(new User(nickname, password));
    }
}