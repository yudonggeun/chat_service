package com.websocket.demo.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class FindChatListRequest {
    private Long roomId;
    @DateTimeFormat
    private LocalDateTime from;
    @DateTimeFormat
    private LocalDateTime to;
}
