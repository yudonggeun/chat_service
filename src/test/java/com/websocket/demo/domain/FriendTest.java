package com.websocket.demo.domain;

import com.websocket.demo.response.FriendInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FriendTest {

    @DisplayName("친구 목록은 빌더를 사용해서 만들 수 있다.")
    @Test
    public void createByBuilder() {
        //given
        var user = new User("my", "1234");
        var friend = new User("fri", "1234");
        Friend friendInfo = createFriend(user, friend);
        //when //then
        assertThat(friendInfo.getUser()).isEqualTo(user);
        assertThat(friendInfo.getName()).isEqualTo(friend.getNickname());
    }

    @DisplayName("친구 정보를 담은 객체를 반환한다.")
    @Test
    public void toInfo() {
        //given
        var user = new User("my", "1234");
        var friend = new User("fri", "1234");
        Friend friendCol = createFriend(user, friend);
        //when
        FriendInfo info = friendCol.toInfo();
        //then
        assertThat(info).extracting("nickname")
                .isEqualTo("fri");
    }

    private Friend createFriend(User user, User friend) {
        var friendInfo = Friend.builder()
                .friend(friend.getNickname())
                .user(user)
                .build();
        return friendInfo;
    }
}