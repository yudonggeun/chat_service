package com.websocket.demo.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ApiResponseTest {

    @DisplayName("성공시 상태 메시지는 success 이다.")
    @Test
    public void success() {
        //given //when
        var response = ApiResponse.success("test data");
        //then
        assertThat(response).extracting("status", "data")
                .containsExactly("success", "test data");
    }

}