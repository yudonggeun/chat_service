package com.websocket.demo.service;

import com.websocket.demo.domain.Chat;
import com.websocket.demo.domain.Room;
import com.websocket.demo.repository.ChatRepository;
import com.websocket.demo.repository.RoomInfoRepository;
import com.websocket.demo.repository.RoomRepository;
import com.websocket.demo.request.*;
import com.websocket.demo.response.ChatInfo;
import com.websocket.demo.response.DeleteChat;
import com.websocket.demo.response.RoomInfo;
import com.websocket.demo.response.RoomUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    public List<ChatInfo> findChatList(FindChatListRequest req) {
        return chatRepository.findByRoomIdAndCreatedAtBetween(
                        req.getRoomId(), req.getFrom(), req.getTo()
                ).stream()
                .map(ChatInfo::from)
                .toList();
    }

    @Transactional(readOnly = true)
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
        if (!roomInfoRepository.existsByUserNicknameAndRoomId(nickname, request.getId())) {
            roomRepository.deleteById(request.getId());
        }

        return RoomUserInfo.builder()
                .roomId(request.getId())
                .nickname(nickname)
                .build();
    }

    public RoomInfo inviteUser(InViteUserRequest request, String host) {

        var room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("해당 채팅방은 존재하지 않습니다."));

        if (!room.containsUser(host)) throw new IllegalArgumentException("해당 유저는 초대 권한이 없습니다.");

        room.addUser(request.getNickname());
        return RoomInfo.fromWithoutChat(room);
    }

    public RoomUserInfo updateRoom(UpdateRoomConfigRequest request, String nickname) {

        var config = roomInfoRepository.findByUserNicknameAndRoomId(nickname, request.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("잘못되 요청입니다."));
        config.setBackgroundColor(request.getBackgroundColor());

        return RoomUserInfo.builder()
                .roomId(config.getRoom().getId())
                .nickname(config.getUserNickname())
                .backgroundColor(config.getBackgroundColor())
                .build();
    }

    public RoomUserInfo checkRoom(CheckRoomRequest request, String nickname) {

        var config = roomInfoRepository.findByUserNicknameAndRoomId(nickname, request.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("잘못되 요청입니다."));
        config.setCheckTime(request.getCheckTime());

        return RoomUserInfo.builder()
                .time(config.getCheckTime())
                .roomId(config.getRoom().getId())
                .nickname(config.getUserNickname())
                .build();
    }
}
