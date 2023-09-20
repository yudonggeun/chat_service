package com.websocket.demo.controller;

import com.websocket.demo.request.FindChatListRequest;
import com.websocket.demo.request.LoginRequest;
import com.websocket.demo.response.ChatInfo;
import com.websocket.demo.response.FriendInfo;
import com.websocket.demo.response.RoomInfo;
import com.websocket.demo.service.ChatService;
import com.websocket.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class PageController {

    private final ChatService chatService;
    private final UserService userService;
    @RequestMapping("/")
    public String mainPage(@SessionAttribute(name = "user", required = false) LoginRequest loginInfo, Model model,
                           @RequestParam(required = false) Long roomId) {
        if(loginInfo == null) return "login";
        String nickname = loginInfo.getNickname();
        List<RoomInfo> roomList = chatService.findRoomList(nickname);
        List<FriendInfo> friends = userService.friendList(nickname);

        FindChatListRequest request = new FindChatListRequest();
        request.setRoomId(roomId);

        List<ChatInfo> chatList = chatService.findChatList(request);
        RoomInfo roomInfo = null;
        for (RoomInfo info : roomList) {
            if(info.getId().equals(roomId)) {
                roomInfo = info;
            }
        }

        model.addAttribute("nickname", nickname);
        model.addAttribute("roomList", roomList);
        model.addAttribute("friends", friends);
        model.addAttribute("chatList", chatList);
        model.addAttribute("targetRoom", roomInfo);

        return "index";
    }
}
