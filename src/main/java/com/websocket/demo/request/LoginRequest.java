package com.websocket.demo.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginRequest implements Serializable {
    private String nickname;
    private String password;
}
