package com.websocket.demo;

import com.websocket.demo.interceptor.ChatHandshakeInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.session.Session;
import org.springframework.session.web.socket.config.annotation.AbstractSessionWebSocketMessageBrokerConfigurer;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractSessionWebSocketMessageBrokerConfigurer{

    @Value("${spring.rabbitmq.host}")
    private String host;
    @Autowired
    ChatHandshakeInterceptor chatHandshakeInterceptor;

    @Override
    protected void configureStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chatting")
                .setAllowedOrigins("*")
                .addInterceptors(chatHandshakeInterceptor);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app")
                .enableStompBrokerRelay("/topic")
                .setRelayHost(host)
                .setVirtualHost("/")
                .setRelayPort(61613);
    }

}
