package com.websocket.demo.interceptor;

import com.websocket.demo.request.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.server.ServletServerHttpRequest;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.atIndex;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;

@ExtendWith(MockitoExtension.class)
class ChatHandshakeInterceptorTest {

    @DisplayName("로그인이 된 상태에서 websocket 연결이 진행하면 닉네임 정보를 attribute 에 저장한다.")
    @Test
    public void beforeHandshakeWhenLogin() {
        //given
        var loginInfo = new LoginRequest();
        loginInfo.setNickname("hello");
        loginInfo.setPassword("1234");
        var request = mock(ServletServerHttpRequest.class);
        var servletRequest = mock(HttpServletRequest.class);
        var session = mock(HttpSession.class);
        given(request.getServletRequest()).willReturn(servletRequest);
        given(servletRequest.getSession()).willReturn(session);
        given(session.getAttribute("user")).willReturn(loginInfo);
        var map = new HashMap<String, Object>();

        //when
        boolean isSuccess = new ChatHandshakeInterceptor().beforeHandshake(request, null, null, map);
        //then
        assertThat(isSuccess).isTrue();
        assertThat(map.get("nickname")).isEqualTo("hello");
    }

    @DisplayName("로그인 상태가 아니라면 false를 반환하여 이후 절차를 진행하지 않는다.")
    @Test
    public void beforeHandshakeWhenNotLogin() {
        //given
        var request = mock(ServletServerHttpRequest.class);
        var servletRequest = mock(HttpServletRequest.class);
        given(request.getServletRequest()).willReturn(servletRequest);
        given(servletRequest.getSession()).willReturn(mock(HttpSession.class));
        //when
        boolean isSuccess = new ChatHandshakeInterceptor().beforeHandshake(request, null, null, null);
        //then
        assertThat(isSuccess).isFalse();
    }

}