package com.websocket.demo.repository;

import com.websocket.demo.SpringTest;
import com.websocket.demo.domain.Friend;
import com.websocket.demo.domain.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FriendRepositoryTest extends SpringTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    FriendRepository friendRepository;

    @DisplayName("유저 닉네임으로 유저 정보를 조회한다.")
    @Test
    public void findByUserNickname() {
        //given
        User user1 = saveUser("hello1", "1234");
        User user2 = saveUser("hello2", "1234");
        user1.addFriends(user2);
        userRepository.saveAndFlush(user1);
        //when
        List<Friend> friends = friendRepository.findByUserNickname(user1.getNickname());
        //then
        assertThat(friends).extracting("user.nickname")
                .contains(user1.getNickname());
        assertThat(friends).extracting("friendNickname")
                .contains(user2.getNickname());
    }

    @Transactional
    @DisplayName("유저 닉네임과 친구 닉네임이 일치하는 친구 목록을 삭제한다.")
    @Test
    public void deleteByUserNicknameAndFriendNickName() {
        //given
        User user1 = saveUser("hello1", "1234");
        User user2 = saveUser("hello2", "1234");
        user1.addFriends(user2);
        userRepository.saveAndFlush(user1);
        //when
        friendRepository.deleteByUserNicknameAndFriendNickname(user1.getNickname(), user2.getNickname());
        //then
        assertThat(friendRepository.findByUserNickname(user1.getNickname())).hasSize(0);
    }

    private User saveUser(String nickname, String password) {
        return userRepository.saveAndFlush(
                User.builder()
                        .nickname(nickname)
                        .password(password).build()
        );
    }
}