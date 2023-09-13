package com.websocket.demo.service;

import com.websocket.demo.request.CreateUserRequest;
import com.websocket.demo.request.LoginRequest;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public boolean login(LoginRequest request) {
        return false;
    }

    public boolean create(CreateUserRequest request) {
        return false;
    }
}
