package com.websocket.demo.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse {

    private final String status;
    private final Object data;
    public static ApiResponse success(Object result) {
        return new ApiResponse("success", result);
    }
}
