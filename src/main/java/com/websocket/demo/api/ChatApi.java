package com.websocket.demo.api;

import com.websocket.demo.request.FindChatListRequest;
import com.websocket.demo.response.ApiResponse;
import com.websocket.demo.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ChatApi {

    private final ChatService chatService;

    @GetMapping("/chat")
    public ApiResponse getChattingList(@ModelAttribute FindChatListRequest request){
        return ApiResponse.success(chatService.findChatList(request));
    }
}
