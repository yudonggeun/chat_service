package com.websocket.demo.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeleteFriendRequest {
    private String friendNickname;
}
