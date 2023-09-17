package com.websocket.demo.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChatStompResponseTest {

    @DisplayName("웹소켓 채팅 생성 성공 응답 생성")
    @Test
    public void createChat() {
        //given
        var chatInfo = new ChatInfo();
        //when
        ChatStompResponse response = ChatStompResponse.createChat(chatInfo);
        //then
        assertThat(response).extracting("type", "data")
                .containsExactly("createChat", chatInfo);
    }

    @DisplayName("웹소켓 채팅 삭제 성공 응답 생성")
    @Test
    public void deleteChat() {
        //given
        var data = new DeleteChat(100L, 1L);
        //when
        ChatStompResponse response = ChatStompResponse.deleteChat(data);
        //then
        assertThat(response).extracting("type", "data")
                .containsExactly("deleteChat", data);
    }

    @DisplayName("웹소켓 채팅방 친구 초대 성공 응답 생성")
    @Test
    public void addFriend() {
        //given
        var data = RoomInfo.builder().build();
        //when
        ChatStompResponse response = ChatStompResponse.friendComeInRoom(data);
        //then
        assertThat(response).extracting("type", "data")
                .containsExactly("friendComeInRoom", data);
    }

    @DisplayName("웹소켓 유저 채팅방 나기기 성공 응답 생성")
    @Test
    public void getOutRoom() {
        //given
        var data = new RoomUserInfo();
        //when
        ChatStompResponse response = ChatStompResponse.getOutRoom(data);
        //then
        assertThat(response).extracting("type", "data")
                .containsExactly("getOutRoom", data);
    }

    @DisplayName("웹소켓 채팅 읽기 성공 응답")
    @Test
    public void readChat() {
        //given
        var data = new RoomUserInfo();
        //when
        ChatStompResponse response = ChatStompResponse.readChat(data);
        //then
        assertThat(response).extracting("type", "data")
                .containsExactly("readChat", data);
    }

}