package com.websocket.demo.service;

import com.websocket.demo.domain.Chat;
import com.websocket.demo.domain.Room;
import com.websocket.demo.repository.ChatRepository;
import com.websocket.demo.repository.RoomInfoRepository;
import com.websocket.demo.repository.RoomRepository;
import com.websocket.demo.request.*;
import com.websocket.demo.response.*;
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
    private final RoomInfoRepository roomInfoRepository;

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
    public RoomUserInfo getOutRoom(RoomOutRequest request, String nickname) {
        roomInfoRepository.deleteByUserNicknameAndRoomId(nickname, request.getId());
        if(!roomInfoRepository.existsByUserNicknameAndRoomId(nickname, request.getId())){
            roomRepository.deleteById(request.getId());
        }

        var result = new RoomUserInfo();
        result.setNickname(nickname);
        result.setRoomId(result.getRoomId());
        return result;
    }
}
