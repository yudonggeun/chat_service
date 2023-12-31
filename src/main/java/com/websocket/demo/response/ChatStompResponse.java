package com.websocket.demo.response;

import lombok.Getter;

@Getter
public class ChatStompResponse {
    private String type;
    private Object data;

    private ChatStompResponse(String type, Object data) {
        this.type = type;
        this.data = data;
    }

    public static ChatStompResponse createChat(ChatInfo data){
        return new ChatStompResponse("createChat", data);
    }

    public static ChatStompResponse deleteChat(DeleteChat data){
        return new ChatStompResponse("deleteChat", data);
    }

    public static ChatStompResponse friendComeInRoom(RoomInfo data){
        return new ChatStompResponse("friendComeInRoom", data);
    }

    public static ChatStompResponse getOutRoom(RoomUserInfo data) {
        return new ChatStompResponse("getOutRoom", data);
    }

    public static ChatStompResponse readChat(RoomUserInfo data) {
        return new ChatStompResponse("readChat", data);
    }
}