package com.websocket.demo.service;

import com.websocket.demo.domain.User;
import com.websocket.demo.repository.UserRepository;
import com.websocket.demo.request.CreateUserRequest;
import com.websocket.demo.request.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public boolean login(LoginRequest request) {
        User findUser = userRepository.findByNickname(request.getNickname());
        return findUser != null && findUser.match(request);
    }

    public boolean create(CreateUserRequest request) {
        try {
            userRepository.save(User.builder()
                    .nickname(request.getNickname())
                    .password(request.getPassword())
                    .build());
            return true;
        } catch (IllegalArgumentException e){
            return false;
        } catch (DataIntegrityViolationException e){
            return false;
        }
    }
}
