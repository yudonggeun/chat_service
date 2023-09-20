package com.websocket.demo.interceptor;

import com.websocket.demo.request.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ChatHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if(isLogin((ServletServerHttpRequest) request)){
            setInformation((ServletServerHttpRequest) request, attributes);
            return true;
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }

    private boolean isLogin(ServletServerHttpRequest request) {
        var servletRequest = request;
        var session = servletRequest.getServletRequest().getSession();
        var info = (LoginRequest) session.getAttribute("user");
        return info != null;
    }

    // spring security 적용시 Principal 주입으로 해결
    private void setInformation(ServletServerHttpRequest request, Map<String, Object> attributes){
        var servletRequest = request;
        var session = servletRequest.getServletRequest().getSession();
        var info = (LoginRequest) session.getAttribute("user");
        var nickname = info.getNickname();
        attributes.put("nickname", nickname);
    }
}
