package com.websocket.demo.response;

import com.websocket.demo.domain.Room;
import com.websocket.demo.domain.RoomUserData;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RoomInfo {

    private final Long id;
    private final String title;
    private final List<String> users;
    private final List<ChatInfo> chat;

    public static RoomInfo from(Room room) {
        return new RoomInfo(
                room.getId(),
                room.getTitle(),
                room.getData().stream()
                        .map(RoomUserData::getUserNickname)
                        .toList(),
                room.getChatList().stream()
                        .map(ChatInfo::from)
                        .toList()
        );
    }
}
