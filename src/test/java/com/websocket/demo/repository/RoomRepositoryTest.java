package com.websocket.demo.repository;

import com.websocket.demo.SpringTest;
import com.websocket.demo.domain.Chat;
import com.websocket.demo.domain.Room;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class RoomRepositoryTest extends SpringTest {
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    ChatRepository chatRepository;

    @DisplayName("특정 유저의 체팅방 목록을 조회한다.")
    @Test
    public void findByDataUserNickname() {
        //given
        saveRoom("room1", "nick");
        saveRoom("room2", "nick");
        saveRoom("room3", "john");
        //when
        List<Room> all = roomRepository.findAll();
        List<Room> rooms = roomRepository.findByDataUserNickname("nick");
        //then
        assertThat(rooms).hasSize(2)
                .extracting("title")
                .contains("room1", "room2");
    }

    @Transactional
    @DisplayName("채팅방에 유저를 추가하면 유저가 채팅방에 가입된다.")
    @Test
    public void addUser() {
        //given
        var id = saveRoom("room1", "nick", "john", "kim").getId();
        //when
        var room = roomRepository.findById(id).get();
        //then
        assertThat(room.getData())
                .extracting("userNickname")
                .contains("nick", "john", "kim");
    }

    private Chat saveChat(Room room, String sender, String message) {
        return chatRepository.saveAndFlush(Chat.builder()
                .message(message)
                .senderNickname(sender)
                .room(room)
                .build());
    }

    private Room saveRoom(String title, String... userNicknames) {
        Room room = Room.builder()
                .title(title)
                .build();

        for (String nickname : userNicknames) {
            room.addUser(nickname);
        }

        return roomRepository.saveAndFlush(room);
    }

}