package com.websocket.demo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse {

    private final String status;
    @JsonInclude(Include.NON_NULL)
    private final Object data;
    public static ApiResponse success(Object result) {
        return new ApiResponse("success", result);
    }
}
