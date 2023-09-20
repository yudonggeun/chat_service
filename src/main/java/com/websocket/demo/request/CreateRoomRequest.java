package com.websocket.demo.request;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CreateRoomRequest {
    private String title;
    @Size(min = 1, message = "채팅방 생성시 유저는 반드시 한 명 이상이어야 합니다.")
    private List<String> users;
}
