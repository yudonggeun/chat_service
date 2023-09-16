package com.websocket.demo.integrate;

import com.websocket.demo.SpringTest;
import com.websocket.demo.interceptor.ChatHandshakeInterceptor;
import com.websocket.demo.request.CreateChatRequest;
import com.websocket.demo.request.CreateRoomRequest;
import com.websocket.demo.response.RoomInfo;
import com.websocket.demo.service.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

public class ChatTest extends SpringTest {

    @Value(value = "${local.server.port}")
    private int port;
    @MockBean
    ChatHandshakeInterceptor chatHandshakeInterceptor;
    @Autowired
    ChatService chatService;

    private WebSocketStompClient stompClient;

    private final WebSocketHttpHeaders headers = new WebSocketHttpHeaders();

    @BeforeEach
    public void setup() {
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        this.stompClient = new WebSocketStompClient(webSocketClient);
        this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    @DisplayName("체팅 요청을 보내면 브로드 케스팅된 체팅 데이터를 읽는다.")
    @Test
    public void getGreeting() throws Exception {

        given(chatHandshakeInterceptor.beforeHandshake(any(), any(), any(), any()))
                .willReturn(true);
        var createRoomRequest = new CreateRoomRequest();
        createRoomRequest.setTitle("welcome");
        createRoomRequest.setUsers(List.of("john"));
        RoomInfo room = chatService.createRoom(createRoomRequest);
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<Throwable> failure = new AtomicReference<>();
        StompSessionHandler handler = new TestSessionHandler(failure) {

            @Override
            public void afterConnected(final StompSession session, StompHeaders connectedHeaders) {
                session.subscribe("/topic/chat/new", new StompFrameHandler() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return CreateChatRequest.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        var chatRequest = (CreateChatRequest) payload;
                        try {
                            assertThat("hello, spring!").isEqualTo(chatRequest.getMessage());
                            assertThat("john").isEqualTo(chatRequest.getSender());
                            assertThat(room.getId()).isEqualTo(chatRequest.getRoomId());
                        } catch (Throwable t) {
                            failure.set(t);
                        } finally {
                            session.disconnect();
                            latch.countDown();
                        }
                    }
                });
                try {
                    var request = new CreateChatRequest();
                    request.setMessage("hello, spring!");
                    request.setSender("john");
                    request.setRoomId(room.getId());
                    session.send("/app/chat/new", request);
                } catch (Throwable t) {
                    failure.set(t);
                    latch.countDown();
                }
            }
        };

        this.stompClient.connectAsync("ws://localhost:{port}/chatting", this.headers, handler, this.port);

        if (latch.await(1, TimeUnit.SECONDS)) {
            if (failure.get() != null) throw new AssertionError("", failure.get());
        } else fail("not received");

    }

    private static class TestSessionHandler extends StompSessionHandlerAdapter {

        private final AtomicReference<Throwable> failure;

        public TestSessionHandler(AtomicReference<Throwable> failure) {
            this.failure = failure;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            this.failure.set(new Exception(headers.toString()));
        }

        @Override
        public void handleException(StompSession s, StompCommand c, StompHeaders h, byte[] p, Throwable ex) {
            this.failure.set(ex);
        }

        @Override
        public void handleTransportError(StompSession session, Throwable ex) {
            this.failure.set(ex);
        }
    }
}
