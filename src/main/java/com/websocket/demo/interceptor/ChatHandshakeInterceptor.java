package com.websocket.demo.interceptor;

import com.websocket.demo.request.LoginRequest;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class ChatHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        return isLogin((ServletServerHttpRequest) request);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }

    private static boolean isLogin(ServletServerHttpRequest request) {
        var servletRequest = request;
        var session = servletRequest.getServletRequest().getSession();
        var info = (LoginRequest) session.getAttribute("user");
        return info != null;
    }
}
