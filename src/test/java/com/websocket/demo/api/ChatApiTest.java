package com.websocket.demo.api;

import com.websocket.demo.controller.RestDocs;
import com.websocket.demo.domain.Chat;
import com.websocket.demo.response.ChatInfo;
import com.websocket.demo.service.ChatService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ChatApiTest extends RestDocs {

    @MockBean
    ChatService chatService;

    @DisplayName("체팅방의 체팅 목록을 조회한다.")
    @Test
    public void getChattingList() throws Exception {
        //given
        var result = ChatInfo.from(createChat(1L, "nickname", "contents..", 100L,
                LocalDateTime.of(1999, 10, 10, 12, 10, 10)));
        given(chatService.findChatList(any())).willReturn(List.of(result));
        //when then
        mockMvc.perform(get("/chat")
                                .queryParam("roomId", "100")
                                .queryParam("from", "1999-10-10T00:00:00")
                                .queryParam("to", "1999-10-11T00:00:00")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpectAll(
                        jsonPath("$.status").value("success"),
                        jsonPath("$.data").exists(),
                        jsonPath("$.data[0].id").value(result.getId()),
                        jsonPath("$.data[0].sender").value(result.getSender()),
                        jsonPath("$.data[0].message").value(result.getMessage()),
                        jsonPath("$.data[0].roomId").value(result.getRoomId()),
                        jsonPath("$.data[0].createdAt").exists()
                )
                .andDo(
                        document("get-chatList",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                queryParameters(
                                        parameterWithName("roomId").description("채팅방 id"),
                                        parameterWithName("from").description("검색 조건 : 체팅 생성 구간 from (format=yyyy-MM-dd)"),
                                        parameterWithName("to").description("검색 조건 : 체팅 생성 구간 to (format=yyyy-MM-dd)")
                                ),
                                responseFields(
                                        fieldWithPath("status").description("요청 처리 결과"),
                                        fieldWithPath("data[].id").description("채팅 id"),
                                        fieldWithPath("data[].sender").description("발신자"),
                                        fieldWithPath("data[].message").description("채팅 내용"),
                                        fieldWithPath("data[].roomId").description("채킹방 id"),
                                        fieldWithPath("data[].createdAt").description("생성시간")
                                )
                        )
                );
    }

    private Chat createChat(long id, String nickname, String message, long roomId, LocalDateTime createdAt) {
        Chat chat = spy(Chat.builder()
                .roomId(roomId)
                .message(message)
                .senderNickname(nickname)
                .build());
        given(chat.getId()).willReturn(id);
        given(chat.getCreatedAt()).willReturn(createdAt);
        return chat;
    }
}