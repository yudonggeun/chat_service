package com.websocket.demo.url;


import com.websocket.demo.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.request.RequestDocumentation.formParameters;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HtmlUrlTest extends RestDocs {

    @MockBean
    UserService userService;
    @DisplayName("회원가입 페이지")
    @Test
    public void createUserPage() throws Exception {
        //given //when //then
        mockMvc.perform(get("/user/create"))
                .andExpect(status().isOk())
                .andDo(document("create-user-page"));
    }

    @DisplayName("메인 페이지")
    @Test
    public void mainPage() throws Exception {
        //given //when //then
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andDo(document("main-page"));
    }

    @DisplayName("유저 생성")
    @Test
    public void createUser() throws Exception {
        //given //when //then
        mockMvc.perform(post("/user/create")
                        .param("nickname", "신기방기")
                        .param("password", "1234"))
                .andExpect(status().is3xxRedirection())
                .andDo(print())
                .andDo(document("create-user-process",
                        formParameters(
                                parameterWithName("nickname").description("닉네임"),
                                parameterWithName("password").description("비밀번호")
                        ),
                        responseHeaders(
                                headerWithName("Location").description("redirect path")
                        )
                ));
    }
    @DisplayName("로그인 처리")
    @Test
    public void login() throws Exception {
        //given //when //then
        mockMvc.perform(post("/user/login")
                        .param("nickname", "신기방기")
                        .param("password", "1234"))
                .andExpect(status().isOk())
                .andDo(document("login-process",
                        formParameters(
                                parameterWithName("nickname").description("닉네임"),
                                parameterWithName("password").description("비밀번호")
                        )
                ));
    }
}
