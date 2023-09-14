package com.websocket.demo.service;

import com.websocket.demo.domain.Friend;
import com.websocket.demo.domain.User;
import com.websocket.demo.repository.FriendRepository;
import com.websocket.demo.repository.UserRepository;
import com.websocket.demo.request.AddFriendRequest;
import com.websocket.demo.request.CreateUserRequest;
import com.websocket.demo.request.LoginRequest;
import com.websocket.demo.response.FriendInfo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    public boolean login(LoginRequest request) {
        User findUser = userRepository.findByNickname(request.getNickname());
        return findUser != null && findUser.match(request);
    }

    public boolean create(CreateUserRequest request) {
        if (userRepository.existsByNickname(request.getNickname())) return false;
        userRepository.save(User.builder()
                .nickname(request.getNickname())
                .password(request.getPassword())
                .build());
        return true;
    }

    public boolean addFriend(AddFriendRequest request, String userNickname) {
        User user = userRepository.findByNickname(userNickname);
        User friend = userRepository.findByNickname(request.getNickname());
        if (friend == null || user == null) return false;
        user.addFriends(friend);
        return true;
    }

    public List<FriendInfo> friendList(String nickname) {
        User user = userRepository.findByNickname(nickname);
        List<Friend> byFieldsUserId = friendRepository.findByUserNickname(user.getNickname());
        return byFieldsUserId.stream().map(Friend::toInfo).toList();
    }

    public void removeFriendByNickname(String userNickname, String friendNickname) {
        friendRepository.deleteByUserNicknameAndFriendNickname(userNickname, friendNickname);
    }
}
