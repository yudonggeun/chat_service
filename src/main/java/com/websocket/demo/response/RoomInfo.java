package com.websocket.demo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.websocket.demo.domain.Room;
import com.websocket.demo.domain.RoomUserData;
import lombok.Builder;
import lombok.Data;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@Data
public class RoomInfo {

    private final Long id;
    private final String title;
    @JsonInclude(Include.NON_NULL)
    private final List<String> users;
    @JsonInclude(Include.NON_NULL)
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

    public static RoomInfo fromWithoutChat(Room room) {
        return RoomInfo.builder()
                .id(room.getId())
                .title(room.getTitle())
                .users(room.getData().stream()
                        .map(RoomUserData::getUserNickname)
                        .toList()
                ).build();
    }
}
