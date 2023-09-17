package com.websocket.demo.response;

import com.websocket.demo.domain.Room;
import com.websocket.demo.domain.RoomUserData;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class RoomInfo {

    private final Long id;
    private final String title;
    private final List<String> users;
    private final List<ChatInfo> chat;

    @Builder
    private RoomInfo(Long id, String title, List<String> users, List<ChatInfo> chat) {
        this.id = id;
        this.title = title;
        this.users = users;
        this.chat = chat;
    }

    public static RoomInfo from(Room room) {
        return RoomInfo.builder()
                .id(room.getId())
                .title(room.getTitle())
                .users(room.getData().stream()
                        .map(RoomUserData::getUserNickname)
                        .toList()
                ).chat(room.getChatList().stream()
                        .map(ChatInfo::from)
                        .toList()
                ).build();
    }
}
