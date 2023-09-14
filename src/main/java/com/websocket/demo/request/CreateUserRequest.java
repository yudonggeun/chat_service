package com.websocket.demo.request;

import lombok.Data;

@Data
public class CreateUserRequest {

    private String nickname;
    private String password;
}
