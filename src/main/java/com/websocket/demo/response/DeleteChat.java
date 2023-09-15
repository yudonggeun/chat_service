package com.websocket.demo.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteChat {
    private Long roomId;
    private Long id;
}
