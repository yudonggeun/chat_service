package com.websocket.demo.repository;

import com.websocket.demo.SpringTest;
import com.websocket.demo.domain.Chat;
import com.websocket.demo.domain.Room;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class ChatRepositoryTest extends SpringTest {

    @Autowired
    ChatRepository chatRepository;
    @Autowired
    RoomRepository roomRepository;

    @DisplayName("체팅이 저장된다.")
    @Test
    public void saveChat() {
        //given
        Room room = saveRoom("room1");
        Chat chat = Chat.builder()
                .room(room)
                .senderNickname("hello")
                .message("hi")
                .build();
        //when
        Chat saved = chatRepository.save(chat);
        //then
        assertThat(saved).isNotNull()
                .extracting("room.id", "senderNickname", "message")
                .containsExactly(room.getId(), "hello", "hi");
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @DisplayName("주어진 id의 체팅을 삭제한다.")
    @Test
    public void deleteOneById() {
        //given
        Room room = saveRoom("room1");
        Chat chat = chatRepository.save(Chat.builder()
                .room(room)
                .senderNickname("hello")
                .message("hi")
                .build());
        //when
        chatRepository.deleteById(100L);
        //then
        assertThat(chatRepository.findById(100L)).isEmpty();
    }

    @DisplayName("특정 기간의 체팅을 조회한다.")
    @Test
    public void findByRoomIdAndCreatedAtBetween() {
        //given
        Room room = roomRepository.saveAndFlush(Room.builder()
                .title("test room")
                .build());

        Chat chat = chatRepository.save(Chat.builder()
                .room(room)
                .senderNickname("hello")
                .message("hi")
                .build());
        var from = LocalDateTime.of(1999, 10, 11, 0, 0, 0);
        var to = LocalDateTime.of(2030, 10, 11, 0, 0, 0);
        //when
        List<Chat> result = chatRepository.findByRoomIdAndCreatedAtBetween(room.getId(), from, to);
        //then
        assertThat(result).extracting("senderNickname", "message", "room.id")
                .containsExactly(
                        tuple("hello", "hi", room.getId())
                );
    }

    @DisplayName("무의미한 기간의 체팅을 조회시 빈 리스트를 반환한다.")
    @Test
    public void findByRoomIdAndCreatedAtBetweenFail() {
        //given
        Room room = roomRepository.saveAndFlush(Room.builder()
                .title("test room")
                .build());

        Chat chat = chatRepository.save(Chat.builder()
                .room(room)
                .senderNickname("hello")
                .message("hi")
                .build());
        var from = LocalDateTime.of(1999, 10, 11, 0, 0, 0);
        var to = LocalDateTime.of(1999, 10, 12, 0, 0, 0);
        //when
        List<Chat> result = chatRepository.findByRoomIdAndCreatedAtBetween(room.getId(), from, to);
        //then
        assertThat(result).extracting("senderNickname", "message", "roomId")
                .isEmpty();
    }

    private Room saveRoom(String title) {
        return roomRepository.saveAndFlush(Room.builder()
                .title(title)
                .build()
        );
    }
}