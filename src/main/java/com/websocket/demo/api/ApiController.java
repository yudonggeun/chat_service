package com.websocket.demo.api;

import com.websocket.demo.request.*;
import com.websocket.demo.response.ApiResponse;
import com.websocket.demo.service.ChatService;
import com.websocket.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.websocket.demo.response.ApiResponse.success;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ApiController {

    private final ChatService chatService;
    private final UserService userService;

    @GetMapping("/chat")
    public ApiResponse getChattingList(@ModelAttribute FindChatListRequest request) {
        return success(chatService.findChatList(request));
    }

    @GetMapping("/room")
    public ApiResponse getRoomList(@SessionAttribute("user") LoginRequest userInfo) {
        return success(chatService.findRoomList(userInfo.getNickname()));
    }

    @PostMapping("/room")
    public ApiResponse createRoomList(@RequestBody CreateRoomRequest request) {
        return success(chatService.createRoom(request));
    }

    @PutMapping("/room")
    public ApiResponse updateRoomConfig(@RequestBody UpdateRoomConfigRequest request, @SessionAttribute("user") LoginRequest userInfo) {
        return success(chatService.updateRoom(request, userInfo.getNickname()));
    }

    @DeleteMapping("/friend")
    public ApiResponse deleteFriend(@RequestBody DeleteFriendRequest request, @SessionAttribute("user") LoginRequest userInfo){
        userService.removeFriendByNickname(userInfo.getNickname(), request.getFriendNickname());
        return success(null);
    }
}
