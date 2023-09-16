package com.websocket.demo.service;

import com.websocket.demo.domain.Chat;
import com.websocket.demo.domain.Room;
import com.websocket.demo.repository.ChatRepository;
import com.websocket.demo.repository.RoomRepository;
import com.websocket.demo.request.CreateChatRequest;
import com.websocket.demo.request.CreateRoomRequest;
import com.websocket.demo.request.DeleteChatRequest;
import com.websocket.demo.request.FindChatListRequest;
import com.websocket.demo.response.ChatInfo;
import com.websocket.demo.response.DeleteChat;
import com.websocket.demo.response.RoomInfo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final ChatRepository chatRepository;
    private final RoomRepository roomRepository;

    public ChatInfo createChat(CreateChatRequest request) {
        var room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 채팅방 입니다."));
        var chat = chatRepository.saveAndFlush(Chat.builder()
                .senderNickname(request.getSender())
                .room(room)
                .message(request.getMessage())
                .build()
        );
        return ChatInfo.from(chat);
    }

    public DeleteChat delete(DeleteChatRequest request) {
        chatRepository.deleteById(request.getId());
        return new DeleteChat(request.getRoomId(), request.getId());
    }

    public List<ChatInfo> findChatList(FindChatListRequest req) {
        return chatRepository.findByRoomIdAndCreatedAtBetween(
                        req.getRoomId(), req.getFrom(), req.getTo()
                ).stream()
                .map(ChatInfo::from)
                .toList();
    }

    public List<RoomInfo> findRoomList(String nickname) {
        List<Room> rooms = roomRepository.findByDataUserNickname(nickname);
        return rooms.stream().map(RoomInfo::from).toList();
    }

    public RoomInfo createRoom(CreateRoomRequest request) {
        Room room = Room.builder()
                .title(request.getTitle())
                .build();
        request.getUsers().forEach(room::addUser);

        return RoomInfo.from(roomRepository.save(room));
    }
}
